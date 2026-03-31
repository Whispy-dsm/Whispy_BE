# Cloudflare R2 Upload Migration Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate file uploads from local filesystem storage to Cloudflare R2 while preserving the current upload API, delete API, and `/file/**` access contract.

**Architecture:** Keep validation, file naming, and image conversion in the existing file application service, but move persistence behind a new `FileStoragePort`. Add two adapters: one for local disk and one for R2. Preserve outward URLs by continuing to return `baseUrl + "/file/{folder}/{fileName}"`; when R2 is active, serve those URLs through a compatibility read path that redirects to the R2 public/custom-domain object URL.

**Tech Stack:** Spring Boot 3.4.5, Java 21, existing hexagonal file domain, AWS SDK for Java v2 S3 client/presigner, Cloudflare R2, JUnit 5, Mockito, existing `FileProperties` configuration.

---

## Repo Facts To Preserve

- Current upload endpoint: `src/main/java/whispy_server/whispy/domain/file/adapter/in/web/controller/FileController.java`
- Current upload implementation writes directly to disk in `src/main/java/whispy_server/whispy/domain/file/application/service/FileUploadService.java`
- Current delete implementation deletes directly from disk in `src/main/java/whispy_server/whispy/domain/file/application/service/FileDeleteService.java`
- Current file URL contract is built in `src/main/java/whispy_server/whispy/domain/file/application/service/FileUploadService.java` as `baseUrl + "/file/" + folder + "/" + fileName`
- Current public file serving is mapped in `src/main/java/whispy_server/whispy/global/config/web/WebConfig.java` from `/file/**` to `file:{uploadPath}/`
- Current storage config lives in `src/main/java/whispy_server/whispy/global/file/FileProperties.java` and `src/main/resources/application.yml`
- Current file validation rules live in `src/main/java/whispy_server/whispy/domain/file/application/utils/FileValidator.java`
- Current upload tests live in `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileUploadServiceTest.java`
- Current delete tests live in `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileDeleteServiceTest.java`

## Assumptions Locked In For This Plan

- Keep **server-mediated uploads** for this migration. Do **not** switch to direct browser-to-R2 presigned `PUT` in the same change.
- Preserve the current public URL shape (`/file/{folder}/{fileName}`) to avoid breaking existing persisted URLs and clients.
- Treat uploaded assets as **public-read** through a controlled R2 custom/public domain once R2 is active. If private reads are required later, plan a separate presigned-download project.
- Use object keys based on the existing folder/file structure: `{folder}/{fileName}`.
- Add feature-flagged cutover and a reversible fallback to local disk until migration verification is complete.

## Planned File Map

- Modify: `build.gradle` - add AWS SDK v2 S3 dependency set
- Modify: `src/main/resources/application.yml` - add storage backend + R2 config placeholders
- Modify: `src/main/java/whispy_server/whispy/global/file/FileProperties.java` - carry backend selection and preserve existing base URL settings
- Create: `src/main/java/whispy_server/whispy/global/file/StorageBackend.java` - backend toggle enum (`LOCAL`, `R2`)
- Create: `src/main/java/whispy_server/whispy/global/file/R2Properties.java` - bind endpoint/bucket/credentials/public URL config
- Create: `src/main/java/whispy_server/whispy/global/config/storage/R2StorageConfig.java` - construct `S3Client` and `S3Presigner`
- Create: `src/main/java/whispy_server/whispy/domain/file/application/port/out/FileStoragePort.java` - storage abstraction for store/delete/public URL/exists
- Create: `src/main/java/whispy_server/whispy/domain/file/adapter/out/storage/LocalFileStorageAdapter.java` - local filesystem implementation extracted from current services
- Create: `src/main/java/whispy_server/whispy/domain/file/adapter/out/storage/CloudflareR2FileStorageAdapter.java` - R2 implementation via AWS SDK v2
- Modify: `src/main/java/whispy_server/whispy/domain/file/application/service/FileUploadService.java` - keep validation/conversion but delegate persistence to port
- Modify: `src/main/java/whispy_server/whispy/domain/file/application/service/FileDeleteService.java` - delegate delete to port
- Create: `src/main/java/whispy_server/whispy/domain/file/adapter/in/web/controller/FileServeController.java` - preserve `/file/{folder}/{fileName}` behavior when backend is R2
- Modify: `src/main/java/whispy_server/whispy/global/config/web/WebConfig.java` - keep local static mapping only for local backend or simplify once compatibility controller is active
- Create: `src/main/java/whispy_server/whispy/domain/file/application/service/FileR2BackfillService.java` - scan local upload directories and upload missing objects
- Create: `src/main/java/whispy_server/whispy/global/config/storage/FileR2BackfillRunner.java` - gated admin/runner entrypoint for dry-run and real backfill
- Modify: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileUploadServiceTest.java` - preserve URL/file-name behavior while mocking storage port
- Modify: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileDeleteServiceTest.java` - preserve delete contract while mocking storage port
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/out/storage/unit/LocalFileStorageAdapterTest.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/out/storage/unit/CloudflareR2FileStorageAdapterTest.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/unit/FileServeControllerTest.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileR2BackfillServiceTest.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/integration/FileMigrationContractTest.java`

### Task 1: Freeze The Current Contract With Tests

**Files:**
- Modify: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileUploadServiceTest.java`
- Modify: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileDeleteServiceTest.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/unit/FileServeControllerTest.java`

- [ ] **Step 1: Write the failing compatibility tests**

```java
@Test
@DisplayName("R2 전환 후에도 업로드 응답 URL 형식은 유지된다")
void uploadFile_keepsExistingPublicUrlShape() {
    assertThat(response.fileUrl())
        .matches("https://example.com/file/profile_image_folder/[a-f0-9\\-]+\\.webp");
}

@Test
@DisplayName("R2 백엔드 활성화 시 /file 요청은 외부 공개 URL로 리다이렉트된다")
void serveFile_redirectsToPublicR2UrlWhenBackendIsR2() {
    assertThat(response.getStatusCode().value()).isEqualTo(302);
    assertThat(response.getHeaders().getLocation())
        .hasToString("https://cdn.example.com/profile_image_folder/test.webp");
}
```

- [ ] **Step 2: Run tests to verify the new assertions fail before implementation**

Run: `gradlew.bat test --tests "*FileUploadServiceTest" --tests "*FileServeControllerTest"`
Expected: FAIL because no compatibility read controller exists and service still hardcodes local persistence assumptions.

- [ ] **Step 3: Tighten existing edge-case coverage before refactor**

```java
@Test
@DisplayName("announcement banner 폴더도 이미지 규칙을 따라 URL이 생성된다")
void uploadFile_supportsAnnouncementBannerFolder() {
    // lock intended behavior before refactor
}

@Test
@DisplayName("webp 원본 업로드는 재압축하지 않는다")
void uploadFile_keepsOriginalWebp() {
    // verify passthrough branch
}
```

- [ ] **Step 4: Re-run the focused test set**

Run: `gradlew.bat test --tests "*FileUploadServiceTest" --tests "*FileDeleteServiceTest" --tests "*FileServeControllerTest"`
Expected: Existing tests pass, new compatibility tests fail.

- [ ] **Step 5: Commit**

```bash
git add src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileUploadServiceTest.java src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileDeleteServiceTest.java src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/unit/FileServeControllerTest.java
git commit -m "test : 파일 스토리지 마이그레이션 계약 테스트 추가"
```

### Task 2: Introduce A Storage Port And Extract The Local Adapter

**Files:**
- Create: `src/main/java/whispy_server/whispy/domain/file/application/port/out/FileStoragePort.java`
- Create: `src/main/java/whispy_server/whispy/domain/file/adapter/out/storage/LocalFileStorageAdapter.java`
- Modify: `src/main/java/whispy_server/whispy/domain/file/application/service/FileUploadService.java`
- Modify: `src/main/java/whispy_server/whispy/domain/file/application/service/FileDeleteService.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/out/storage/unit/LocalFileStorageAdapterTest.java`
- Modify: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileUploadServiceTest.java`
- Modify: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileDeleteServiceTest.java`

- [ ] **Step 1: Write the failing local adapter test**

```java
@Test
@DisplayName("local adapter는 key에 해당하는 경로로 파일을 저장한다")
void store_writesBytesToConfiguredUploadPath() {
    adapter.store("profile_image_folder/test.webp", inputStream, "image/webp");
    assertThat(tempDir.resolve("profile_image_folder/test.webp")).exists();
}
```

- [ ] **Step 2: Run the adapter test to verify it fails**

Run: `gradlew.bat test --tests "*LocalFileStorageAdapterTest"`
Expected: FAIL because `FileStoragePort` and `LocalFileStorageAdapter` do not exist.

- [ ] **Step 3: Add the storage port and local adapter with the minimal contract**

```java
public interface FileStoragePort {
    void store(String key, InputStream inputStream, String contentType);
    void delete(String key);
    URI resolvePublicUri(String key);
    boolean exists(String key);
}

@Component
@RequiredArgsConstructor
class LocalFileStorageAdapter implements FileStoragePort {
    private final FileProperties fileProperties;

    @Override
    public void store(String key, InputStream inputStream, String contentType) {
        Path target = Paths.get(fileProperties.uploadPath(), key);
        Files.createDirectories(target.getParent());
        Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void delete(String key) {
        Files.deleteIfExists(Paths.get(fileProperties.uploadPath(), key));
    }
}
```

- [ ] **Step 4: Refactor the existing services to delegate persistence only**

```java
String key = folder + "/" + fileName;
fileStoragePort.store(key, compressedImage, "image/webp");
return new FileUploadResponse(fileProperties.baseUrl() + "/file/" + key);
```

- [ ] **Step 5: Run the focused tests and commit**

Run: `gradlew.bat test --tests "*FileUploadServiceTest" --tests "*FileDeleteServiceTest" --tests "*LocalFileStorageAdapterTest"`
Expected: PASS with unchanged outward URL behavior.

```bash
git add build.gradle src/main/java/whispy_server/whispy/domain/file/application/port/out/FileStoragePort.java src/main/java/whispy_server/whispy/domain/file/adapter/out/storage/LocalFileStorageAdapter.java src/main/java/whispy_server/whispy/domain/file/application/service/FileUploadService.java src/main/java/whispy_server/whispy/domain/file/application/service/FileDeleteService.java src/test/java/whispy_server/whispy/domain/file/adapter/out/storage/unit/LocalFileStorageAdapterTest.java src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileUploadServiceTest.java src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileDeleteServiceTest.java
git commit -m "refactor : 파일 스토리지를 포트로 추상화"
```

### Task 3: Add Cloudflare R2 Configuration And Adapter

**Files:**
- Modify: `build.gradle`
- Modify: `src/main/resources/application.yml`
- Modify: `src/main/java/whispy_server/whispy/global/file/FileProperties.java`
- Create: `src/main/java/whispy_server/whispy/global/file/StorageBackend.java`
- Create: `src/main/java/whispy_server/whispy/global/file/R2Properties.java`
- Create: `src/main/java/whispy_server/whispy/global/config/storage/R2StorageConfig.java`
- Create: `src/main/java/whispy_server/whispy/domain/file/adapter/out/storage/CloudflareR2FileStorageAdapter.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/out/storage/unit/CloudflareR2FileStorageAdapterTest.java`

- [ ] **Step 1: Write the failing R2 adapter test**

```java
@Test
@DisplayName("R2 adapter는 path-style + chunked encoding off 설정으로 putObject를 호출한다")
void store_usesR2CompatibleS3ClientSettings() {
    verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
}
```

- [ ] **Step 2: Run the R2 adapter test to verify it fails**

Run: `gradlew.bat test --tests "*CloudflareR2FileStorageAdapterTest"`
Expected: FAIL because the R2 config and adapter are missing.

- [ ] **Step 3: Add dependencies and bind R2 properties**

```gradle
implementation 'software.amazon.awssdk:s3'
```

```yaml
spring:
  whispy:
    file:
      backend: ${FILE_STORAGE_BACKEND:LOCAL}
      upload-path: ${UPLOAD_PATH}
      base-url: ${BASE_URL}
      r2:
        endpoint: ${R2_ENDPOINT:}
        bucket: ${R2_BUCKET:}
        access-key-id: ${R2_ACCESS_KEY_ID:}
        secret-access-key: ${R2_SECRET_ACCESS_KEY:}
        public-base-url: ${R2_PUBLIC_BASE_URL:}
```

- [ ] **Step 4: Implement the R2 client bean and adapter**

```java
S3Configuration serviceConfiguration = S3Configuration.builder()
    .pathStyleAccessEnabled(true)
    .chunkedEncodingEnabled(false)
    .build();

return S3Client.builder()
    .endpointOverride(URI.create(r2Properties.endpoint()))
    .credentialsProvider(StaticCredentialsProvider.create(credentials))
    .region(Region.of("auto"))
    .serviceConfiguration(serviceConfiguration)
    .build();
```

```java
@Override
public void store(String key, InputStream inputStream, String contentType) {
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(r2Properties.bucket())
            .key(key)
            .contentType(contentType)
            .build(),
        RequestBody.fromInputStream(inputStream, inputLength)
    );
}
```

- [ ] **Step 5: Run tests and commit**

Run: `gradlew.bat test --tests "*CloudflareR2FileStorageAdapterTest" --tests "*FileUploadServiceTest"`
Expected: PASS; R2 bean setup is covered and upload service still returns unchanged URLs.

```bash
git add build.gradle src/main/resources/application.yml src/main/java/whispy_server/whispy/global/file/FileProperties.java src/main/java/whispy_server/whispy/global/file/StorageBackend.java src/main/java/whispy_server/whispy/global/file/R2Properties.java src/main/java/whispy_server/whispy/global/config/storage/R2StorageConfig.java src/main/java/whispy_server/whispy/domain/file/adapter/out/storage/CloudflareR2FileStorageAdapter.java src/test/java/whispy_server/whispy/domain/file/adapter/out/storage/unit/CloudflareR2FileStorageAdapterTest.java
git commit -m "feat : Cloudflare R2 스토리지 어댑터 추가"
```

### Task 4: Preserve `/file/**` Reads With A Compatibility Layer

**Files:**
- Create: `src/main/java/whispy_server/whispy/domain/file/adapter/in/web/controller/FileServeController.java`
- Modify: `src/main/java/whispy_server/whispy/global/config/web/WebConfig.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/unit/FileServeControllerTest.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/integration/FileMigrationContractTest.java`

- [ ] **Step 1: Write the failing read-compatibility tests**

```java
@Test
@DisplayName("local backend일 때는 기존처럼 /file 경로에서 파일을 내려준다")
void getFile_servesLocalResourceWhenBackendIsLocal() {}

@Test
@DisplayName("R2 backend일 때는 /file 경로에서 공개 R2 URL로 리다이렉트한다")
void getFile_redirectsToR2PublicUrlWhenBackendIsR2() {}
```

- [ ] **Step 2: Run the new controller tests to verify they fail**

Run: `gradlew.bat test --tests "*FileServeControllerTest" --tests "*FileMigrationContractTest"`
Expected: FAIL because no compatibility read endpoint exists yet.

- [ ] **Step 3: Implement the compatibility controller with backend-aware behavior**

```java
@GetMapping("/file/{folder}/{fileName}")
public ResponseEntity<?> getFile(@PathVariable String folder, @PathVariable String fileName) {
    String key = folder + "/" + fileName;
    if (fileProperties.backend() == StorageBackend.R2) {
        URI publicUri = fileStoragePort.resolvePublicUri(key);
        return ResponseEntity.status(HttpStatus.FOUND).location(publicUri).build();
    }
    Resource resource = new UrlResource(Paths.get(fileProperties.uploadPath(), key).toUri());
    return ResponseEntity.ok(resource);
}
```

- [ ] **Step 4: Remove or conditionally disable the old static resource mapping for non-local mode**

```java
if (fileProperties.backend() == StorageBackend.LOCAL) {
    registry.addResourceHandler("/file/**")
        .addResourceLocations("file:" + fileProperties.uploadPath() + "/");
}
```

- [ ] **Step 5: Run tests and commit**

Run: `gradlew.bat test --tests "*FileServeControllerTest" --tests "*FileMigrationContractTest" --tests "*FileUploadServiceTest"`
Expected: PASS; old URL shape still works, but reads are now backend-aware.

```bash
git add src/main/java/whispy_server/whispy/domain/file/adapter/in/web/controller/FileServeController.java src/main/java/whispy_server/whispy/global/config/web/WebConfig.java src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/unit/FileServeControllerTest.java src/test/java/whispy_server/whispy/domain/file/adapter/in/web/controller/integration/FileMigrationContractTest.java
git commit -m "feat : /file 호환 조회 레이어 추가"
```

### Task 5: Add An Idempotent Local-To-R2 Backfill Tool

**Files:**
- Create: `src/main/java/whispy_server/whispy/domain/file/application/service/FileR2BackfillService.java`
- Create: `src/main/java/whispy_server/whispy/global/config/storage/FileR2BackfillRunner.java`
- Create: `src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileR2BackfillServiceTest.java`

- [ ] **Step 1: Write the failing backfill tests**

```java
@Test
@DisplayName("dry-run 모드에서는 업로드하지 않고 대상 key만 리포트한다")
void backfill_dryRunOnlyReportsCandidates() {}

@Test
@DisplayName("이미 존재하는 R2 key는 건너뛴다")
void backfill_skipsExistingObjects() {}
```

- [ ] **Step 2: Run the backfill tests to verify they fail**

Run: `gradlew.bat test --tests "*FileR2BackfillServiceTest"`
Expected: FAIL because the backfill service/runner do not exist.

- [ ] **Step 3: Implement a dry-run capable scanner over the current upload path**

```java
Files.walk(Paths.get(fileProperties.uploadPath()))
    .filter(Files::isRegularFile)
    .forEach(path -> {
        String key = uploadRoot.relativize(path).toString().replace("\\", "/");
        if (!fileStoragePort.exists(key)) {
            // dry-run: report only
            // real-run: upload file bytes to R2
        }
    });
```

- [ ] **Step 4: Add a guarded runner for admin execution**

```java
if (!r2Properties.backfillEnabled()) {
    return;
}
backfillService.run(dryRun);
```

- [ ] **Step 5: Run tests and commit**

Run: `gradlew.bat test --tests "*FileR2BackfillServiceTest"`
Expected: PASS; scanner is idempotent and supports dry-run.

```bash
git add src/main/java/whispy_server/whispy/domain/file/application/service/FileR2BackfillService.java src/main/java/whispy_server/whispy/global/config/storage/FileR2BackfillRunner.java src/test/java/whispy_server/whispy/domain/file/application/service/unit/FileR2BackfillServiceTest.java
git commit -m "feat : 로컬 파일 R2 백필 도구 추가"
```

### Task 6: Cut Over Safely, Verify, And Document Rollback

**Files:**
- Modify: `src/main/resources/application.yml`
- Modify: `src/main/java/whispy_server/whispy/global/file/FileProperties.java`
- Modify: `README.md`
- Optionally Create: `document/R2_MIGRATION_RUNBOOK.md`

- [ ] **Step 1: Add explicit cutover checklist and rollback flags to docs/config comments**

```yaml
spring:
  whispy:
    file:
      backend: ${FILE_STORAGE_BACKEND:LOCAL} # LOCAL -> R2 cutover flag
```

- [ ] **Step 2: Run unit and integration verification before any environment switch**

Run: `gradlew.bat test --tests "*FileUploadServiceTest" --tests "*FileDeleteServiceTest" --tests "*LocalFileStorageAdapterTest" --tests "*CloudflareR2FileStorageAdapterTest" --tests "*FileServeControllerTest" --tests "*FileMigrationContractTest" --tests "*FileR2BackfillServiceTest"`
Expected: PASS

- [ ] **Step 3: Run full project verification**

Run: `gradlew.bat test`
Expected: PASS (or explicit note of unrelated pre-existing failures)

- [ ] **Step 4: Perform the operational cutover in this order**

```text
1. Create/verify R2 bucket and custom/public domain.
2. Enable bucket CORS only if future direct browser uploads are planned.
3. Run backfill in dry-run mode.
4. Run backfill in real mode and verify object count/sample access.
5. Switch FILE_STORAGE_BACKEND=R2 in staging.
6. Verify upload/read/delete behavior in staging.
7. Switch production only after rollback plan is documented.
```

- [ ] **Step 5: Commit docs/config changes**

```bash
git add src/main/resources/application.yml README.md document/R2_MIGRATION_RUNBOOK.md
git commit -m "docs : R2 파일 스토리지 전환 런북 추가"
```

## Verification Checklist

- `gradlew.bat test` passes after each task boundary
- New uploads still return URLs shaped like `https://<base>/file/{folder}/{fileName}`
- Local backend still serves historical files without behavior change
- R2 backend redirects `/file/**` to the configured public R2 URL without exposing credentials
- Delete operation removes the correct R2 object key and still returns `204 No Content`
- Backfill supports dry-run, skip-existing, and repeatable runs
- R2 client uses:
  - `endpointOverride(...)`
  - `Region.of("auto")`
  - `pathStyleAccessEnabled(true)`
  - `chunkedEncodingEnabled(false)`

## Out Of Scope For This Plan

- Direct browser-to-R2 uploads with presigned `PUT`
- Private asset authorization with presigned `GET`
- Database-wide rewriting of previously stored file URLs
- CDN optimization beyond preserving current compatibility behavior

## Review Notes

- Prefer a **compatibility read layer** over changing the returned file URL host in the same migration.
- Prefer **server-side upload first**; presigned client upload is a separate optimization project.
- Do not delete local files until:
  - backfill count matches
  - sample downloads succeed
  - rollback window closes
