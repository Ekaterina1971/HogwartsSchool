--liquibase formatted sql

--changeset ekaterina:1
CREATE INDEX name_idx
ON student (name);

--changeset ekaterina:2
CREATE INDEX name_and_color_idx
ON faculty (name, color);