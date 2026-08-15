package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
