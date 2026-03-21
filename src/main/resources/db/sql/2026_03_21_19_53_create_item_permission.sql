--liquibase formatted sql
--changeset dkazmierczak:2026_03_21_19_53_create_item_permission

CREATE TABLE item_permissions (
                                  id VARCHAR(36) PRIMARY KEY,
                                  item_id VARCHAR(36) NOT NULL,
                                  user_id VARCHAR(36) NOT NULL,
                                  role VARCHAR(20) NOT NULL,
                                  granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

                                  CONSTRAINT fk_permissions_item FOREIGN KEY (item_id) REFERENCES items (id),
                                  CONSTRAINT fk_permissions_user FOREIGN KEY (user_id) REFERENCES users (id),
                                  CONSTRAINT uq_item_user UNIQUE (item_id, user_id)
);

CREATE TABLE item_permissions_aud (
                                      id VARCHAR(36) NOT NULL,
                                      revision_id INT NOT NULL,
                                      revision_type INT,

                                      item_id VARCHAR(36),
                                      user_id VARCHAR(36),
                                      role VARCHAR(20),

                                      PRIMARY KEY (id, revision_id),
                                      CONSTRAINT fk_item_permissions_aud_revinfo FOREIGN KEY (revision_id) REFERENCES revinfo (revision_id)
);