package ru.burdak.mainservice.mapper;

import ru.burdak.mainservice.dto.user.NewUserRequest;
import ru.burdak.mainservice.dto.event.ParticipationRequestDto;
import ru.burdak.mainservice.dto.user.UserDto;
import ru.burdak.mainservice.dto.user.UserShortDto;
import ru.burdak.mainservice.model.User;

import java.util.List;

public class UserMapper {
    public static User toEntity(NewUserRequest dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setName(dto.name());
        return user;
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users
            .stream()
            .map(UserMapper::toDto)
            .toList();
    }




}
