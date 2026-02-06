-- ============================================
-- P0: 즉시 추가 필수 인덱스
-- ============================================

-- 1. MusicLike userId 인덱스 추가
ALTER TABLE tbl_music_like
    ADD INDEX idx_music_like_user_id (user_id);

-- 2. ListeningHistory 복합 인덱스 추가 (userId + listened_at)
ALTER TABLE tbl_listening_history
    ADD INDEX idx_listening_history_user_listened (user_id, listened_at);

-- 3. SoundSpaceMusic 복합 인덱스 추가 (userId + added_at)
ALTER TABLE tbl_soundspace_music
    ADD INDEX idx_soundspace_user_added (user_id, added_at);

-- ============================================
-- P1: 성능 최적화 인덱스
-- ============================================

-- 4. Notification 인덱스 재생성 (filesort 제거)
ALTER TABLE tbl_notification
    DROP INDEX idx_notification_email_read,
    ADD INDEX idx_notification_email_read_created (email, `read`, created_at);
