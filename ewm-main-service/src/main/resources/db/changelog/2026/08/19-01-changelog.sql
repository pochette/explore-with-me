-- liquibase formatted sql

-- changeset Andrey_Burdak:1787146071071-1
ALTER TABLE events
    ALTER COLUMN description TYPE VARCHAR(7000) USING (description::VARCHAR(7000));

