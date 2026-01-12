-- 공지사항 테이블에서 banner_image_url 컬럼 제거
-- 마크다운 형식의 content로 이미지 표현 가능하므로 별도 컬럼 불필요

ALTER TABLE tbl_announcement DROP COLUMN banner_image_url;
