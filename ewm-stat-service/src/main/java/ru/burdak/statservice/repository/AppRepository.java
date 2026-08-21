package ru.burdak.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.statservice.model.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {

    Optional<App> findAppByName(String name);
}
