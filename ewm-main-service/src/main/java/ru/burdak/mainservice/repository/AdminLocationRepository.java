package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.AdminLocation;

public interface AdminLocationRepository extends JpaRepository<AdminLocation, Long> {
    boolean existsByName(String name);

    //TODO WHERE distance(
    //    event_location.lat,
    //    event_location.lon,
    //    admin_location.lat,
    //    admin_location.lon)
    // <= admin_location.radius
}
