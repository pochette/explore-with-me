package ru.burdak.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.Compilation;

import java.util.Collection;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Collection<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);


}
