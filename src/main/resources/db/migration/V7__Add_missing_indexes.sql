-- 1. MusicLike userId 인덱스 추가
CREATE INDEX idx_music_like_user_id ON tbl_music_like (user_id);

-- 2. ListeningHistory 복합 인덱스 추가 (userId + listened_at)
CREATE INDEX idx_listening_history_user_listened ON tbl_listening_history (user_id, listened_at);

-- 3. SoundSpaceMusic 복합 인덱스 추가 (userId + added_at)
CREATE INDEX idx_soundspace_user_added ON tbl_soundspace_music (user_id, added_at);

-- 4. Notification 인덱스 추가 (email, read, created_at)
CREATE INDEX idx_notification_email_read_created ON tbl_notification (email, `read`, created_at);
