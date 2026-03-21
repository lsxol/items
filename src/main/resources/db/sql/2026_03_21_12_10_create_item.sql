--liquibase formatted sql
--changeset dkazmierczak:2026_03_21_12_10_create_item

CREATE TABLE items (
                       id VARCHAR(36) PRIMARY KEY,
                       owner_id VARCHAR(36) NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       content TEXT,
                       version INT NOT NULL,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_items_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE items_aud (
                           id VARCHAR(36) NOT NULL,
                           revision_id INT NOT NULL,
                           revision_type int,
                           owner_id VARCHAR(36),
                           title VARCHAR(255),
                           content TEXT,
                           deleted BOOLEAN,

                           PRIMARY KEY (id, revision_id),
                           CONSTRAINT fk_items_aud_revinfo FOREIGN KEY (revision_id) REFERENCES revinfo (revision_id)
);