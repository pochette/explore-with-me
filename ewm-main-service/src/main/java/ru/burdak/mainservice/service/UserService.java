package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.dto.user.NewUserRequest;
import ru.burdak.mainservice.dto.user.UserDto;

import java.util.List;

public interface UserService {
    void deleteUser(HttpServletRequest request, Long id);

    List<UserDto> getUsers(HttpServletRequest request, List<Long> ids, Integer from, Integer size);

    UserDto postNewUser(HttpServletRequest request, NewUserRequest newUserRequest);
}
