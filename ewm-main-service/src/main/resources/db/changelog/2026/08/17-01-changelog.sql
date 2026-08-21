-- liquibase formatted sql

-- changeset Andrey_Burdak:1786984889340-1
ALTER TABLE events
    ALTER COLUMN published_on DROP NOT NULL;

