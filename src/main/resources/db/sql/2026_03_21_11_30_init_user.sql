--liquibase formatted sql
--changeset dkazmierczak:2026_03_21_11_30_init_user

CREATE TABLE users (
                       id VARCHAR(36) PRIMARY KEY,
                       login VARCHAR(64) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);