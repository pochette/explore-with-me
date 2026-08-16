package ru.burdak.mainservice.controller.admin_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.user.NewUserRequest;
import ru.burdak.mainservice.dto.user.UserDto;
import ru.burdak.mainservice.service.UserService;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDto> getUsers(HttpServletRequest request,
                                  @RequestParam(name = "ids", required = false) List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0", required = false) @PositiveOrZero
                                  Integer from,
                                  @RequestParam(name = "size", defaultValue = "10", required = false) @Positive
                                  Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        ids = ids == null ? Collections.emptyList() : ids;
        return userService.getUsers(request, ids, from, size);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto postNewUser(HttpServletRequest request,
                               @RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.postNewUser(request, newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(HttpServletRequest request,
                           @PathVariable("userId") @Positive Long id) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.deleteUser(request, id);
    }
}
