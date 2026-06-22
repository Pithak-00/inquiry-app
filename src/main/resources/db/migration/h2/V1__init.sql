-- ========================================
-- 問い合わせ管理アプリ 初期スキーマ
-- H2 Database (MySQL互換モード)
-- ========================================

CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    email       VARCHAR(255)  NOT NULL,
    password    VARCHAR(255)  NOT NULL,
    role        VARCHAR(10)   NOT NULL DEFAULT 'USER',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tickets (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(100) NOT NULL,
    body         CLOB         NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'NEW',
    priority     VARCHAR(10)  NOT NULL,
    category_id  BIGINT,
    deleted_at   TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tickets_user     FOREIGN KEY (user_id)     REFERENCES users(id),
    CONSTRAINT fk_tickets_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS ticket_comments (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id  BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    body       CLOB     NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    CONSTRAINT fk_comments_user   FOREIGN KEY (user_id)   REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS ticket_attachments (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id     BIGINT        NOT NULL,
    original_name VARCHAR(255)  NOT NULL,
    path          VARCHAR(500)  NOT NULL,
    mime_type     VARCHAR(100)  NOT NULL,
    size          BIGINT        NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attachments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

CREATE TABLE IF NOT EXISTS ticket_status_logs (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id    BIGINT NOT NULL,
    changed_by   BIGINT NOT NULL,
    from_status  VARCHAR(20),
    to_status    VARCHAR(20) NOT NULL,
    changed_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_logs_ticket FOREIGN KEY (ticket_id)  REFERENCES tickets(id),
    CONSTRAINT fk_logs_user   FOREIGN KEY (changed_by) REFERENCES users(id)
);

-- インデックス
CREATE INDEX IF NOT EXISTS idx_tickets_user_id    ON tickets(user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_status      ON tickets(status);
CREATE INDEX IF NOT EXISTS idx_tickets_priority    ON tickets(priority);
CREATE INDEX IF NOT EXISTS idx_tickets_category_id ON tickets(category_id);
CREATE INDEX IF NOT EXISTS idx_tickets_created_at  ON tickets(created_at);
CREATE INDEX IF NOT EXISTS idx_tickets_deleted_at  ON tickets(deleted_at);
CREATE INDEX IF NOT EXISTS idx_comments_ticket_id  ON ticket_comments(ticket_id);
CREATE INDEX IF NOT EXISTS idx_attach_ticket_id    ON ticket_attachments(ticket_id);
CREATE INDEX IF NOT EXISTS idx_logs_ticket_id      ON ticket_status_logs(ticket_id);
