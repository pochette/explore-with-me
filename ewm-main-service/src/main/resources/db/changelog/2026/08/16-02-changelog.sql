-- liquibase formatted sql

-- changeset Andrey_Burdak:1786895777855-1
ALTER TABLE public.users
    ALTER COLUMN email TYPE VARCHAR(254) USING (email::VARCHAR(254));

-- changeset Andrey_Burdak:1786895777855-2
ALTER TABLE public.users
    ALTER COLUMN name TYPE VARCHAR(250) USING (name::VARCHAR(250));

