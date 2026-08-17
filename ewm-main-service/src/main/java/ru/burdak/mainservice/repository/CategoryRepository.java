package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.burdak.mainservice.model.Category;

@RepositoryRestResource(path = "categories")
public interface CategoryRepository extends JpaRepository <Category, Long> {

    boolean existsByIdNot(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
