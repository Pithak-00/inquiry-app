-- ========================================
-- テスト用初期データ（PostgreSQL用）
-- パスワードはすべて "password123" (BCrypt hash)
-- ========================================

-- カテゴリ
INSERT INTO categories (name) VALUES ('技術的な問題');
INSERT INTO categories (name) VALUES ('請求・料金');
INSERT INTO categories (name) VALUES ('アカウント');
INSERT INTO categories (name) VALUES ('その他');

-- ユーザー（管理者）
INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
('管理者 太郎', 'admin@example.com',
 '$2b$10$oincbgt8mxEUKgBpGSaxWOBDL6hssscArdM49FYNr5iWHYdkRwmqS',
 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ユーザー（一般）
INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
('田中 花子', 'user1@example.com',
 '$2b$10$E6UlJUzXV7Sn/0Sk3LqiwuZDcZciB1zLzERsW2NhXwXrHLheR4Qdm',
 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
('山田 次郎', 'user2@example.com',
 '$2b$10$PtAzqYVHkbq7od2CJ7IIoOAk0tn3gzK79Tv3eJ3vCD7tJT9Wn2sAW',
 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- サンプルチケット
INSERT INTO tickets (user_id, title, body, status, priority, category_id, created_at, updated_at) VALUES
(2, 'ログインできない', 'パスワードを正しく入力しているのにログインできません。エラーメッセージは「認証に失敗しました」と表示されます。', 'NEW', 'HIGH', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (user_id, title, body, status, priority, category_id, created_at, updated_at) VALUES
(2, '請求書の金額が合わない', '先月の請求書に誤りがあるようです。契約プランより高い金額が請求されています。確認をお願いします。', 'IN_PROGRESS', 'MEDIUM', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (user_id, title, body, status, priority, category_id, created_at, updated_at) VALUES
(3, 'ページが表示されない', 'トップページにアクセスすると500エラーが表示されます。昨日から発生しています。', 'RESOLVED', 'HIGH', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (user_id, title, body, status, priority, category_id, created_at, updated_at) VALUES
(3, 'パスワード変更方法を教えてください', 'パスワードを変更したいのですが、設定画面が見つかりません。', 'CLOSED', 'LOW', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- サンプルコメント
INSERT INTO ticket_comments (ticket_id, user_id, body, created_at) VALUES
(2, 1, 'ご連絡ありがとうございます。現在確認中です。しばらくお待ちください。', CURRENT_TIMESTAMP);

INSERT INTO ticket_comments (ticket_id, user_id, body, created_at) VALUES
(2, 2, '承知しました。よろしくお願いします。', CURRENT_TIMESTAMP);

-- サンプルステータスログ
INSERT INTO ticket_status_logs (ticket_id, changed_by, from_status, to_status, changed_at) VALUES
(2, 1, 'NEW', 'IN_PROGRESS', CURRENT_TIMESTAMP);

INSERT INTO ticket_status_logs (ticket_id, changed_by, from_status, to_status, changed_at) VALUES
(3, 1, 'NEW', 'IN_PROGRESS', CURRENT_TIMESTAMP);

INSERT INTO ticket_status_logs (ticket_id, changed_by, from_status, to_status, changed_at) VALUES
(3, 1, 'IN_PROGRESS', 'RESOLVED', CURRENT_TIMESTAMP);

INSERT INTO ticket_status_logs (ticket_id, changed_by, from_status, to_status, changed_at) VALUES
(4, 1, 'NEW', 'IN_PROGRESS', CURRENT_TIMESTAMP);

INSERT INTO ticket_status_logs (ticket_id, changed_by, from_status, to_status, changed_at) VALUES
(4, 1, 'IN_PROGRESS', 'RESOLVED', CURRENT_TIMESTAMP);

INSERT INTO ticket_status_logs (ticket_id, changed_by, from_status, to_status, changed_at) VALUES
(4, 1, 'RESOLVED', 'CLOSED', CURRENT_TIMESTAMP);
