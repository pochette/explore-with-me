package ru.burdak.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.burdak.mainservice.model.User;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    List<User> findAllBy(Pageable pageable);

    List<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);

}