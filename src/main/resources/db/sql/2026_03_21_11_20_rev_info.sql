--liquibase formatted sql
--changeset dkazmierczak:2026_03_21_11_20_rev_info

CREATE TABLE revinfo (
                         revision_id INT AUTO_INCREMENT PRIMARY KEY,
                         revtstmp BIGINT,
                         changed_by VARCHAR(64)
);