package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.user.NewUserRequest;
import ru.burdak.mainservice.dto.user.UserDto;
import ru.burdak.mainservice.exception.ConflictException;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.UserMapper;
import ru.burdak.mainservice.model.User;
import ru.burdak.mainservice.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void deleteUser(HttpServletRequest request, Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id=" + id + " was not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers(HttpServletRequest request, List<Long> ids, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort
            .by("id")
            .ascending());

        List<User> users = ids == null || ids.isEmpty() ?
            userRepository.findAllBy(pageable) :
            userRepository.findAllByIdIn(ids, pageable);
        return users
            .stream()
            .map(UserMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public UserDto postNewUser(HttpServletRequest request, NewUserRequest newUserRequest) {
        if (existsByEmail(newUserRequest.email())) {
            log.info("Нарушение целостности данных");
            throw new ConflictException("User with email=" + newUserRequest.email() + " already exists");
        }

        User user = UserMapper.toEntity(newUserRequest);
        return UserMapper.toDto(userRepository.save(user));
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);

    }
}
