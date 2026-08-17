package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
