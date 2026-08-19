package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

}
