package ru.burdak.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.statservice.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long> {
}
