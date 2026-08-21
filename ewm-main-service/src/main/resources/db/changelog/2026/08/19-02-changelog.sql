-- liquibase formatted sql

-- changeset Andrey_Burdak:1787146259989-1
ALTER TABLE events
    ALTER COLUMN annotation TYPE VARCHAR(2000) USING (annotation::VARCHAR(2000));

-- changeset Andrey_Burdak:1787146259989-2
ALTER TABLE events
    ALTER COLUMN title TYPE VARCHAR(120) USING (title::VARCHAR(120));

