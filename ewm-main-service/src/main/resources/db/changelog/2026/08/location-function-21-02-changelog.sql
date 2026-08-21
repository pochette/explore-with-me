-- liquibase formatted sql

-- changeset burda:location-distance-function-1
CREATE OR REPLACE FUNCTION distance(
    lat1 float,
    lon1 float,
    lat2 float,
    lon2 float)
    returns float
    LANGUAGE plpgsql
    IMMUTABLE
    STRICT
AS
'
    declare
        dist      float = 0;
        rad_lat1  float;
        rad_lat2  float;
        theta     float;
        rad_theta float;
    BEGIN
        IF lat1 = lat2 AND lon1 = lon2
        THEN
            RETURN dist;

        ELSE
            rad_lat1 := pi() * lat1 / 180;
            rad_lat2 := pi() * lat2 / 180;

            theta = lon1 - lon2;

            rad_theta := pi() * theta / 180;
            dist :=
                    sin(rad_lat1) * sin(rad_lat2)
                        + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

            IF dist > 1 THEN
                dist := 1;
            ELSEIF dist < 1 THEN
                dist := -1;
            END IF;

            dist := 6371000 * acos(dist);
            RETURN dist;

        end IF;
END;
'