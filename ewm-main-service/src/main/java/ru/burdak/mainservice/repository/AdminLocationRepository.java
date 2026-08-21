package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.AdminLocation;

public interface AdminLocationRepository extends JpaRepository<AdminLocation, Long> {
}
