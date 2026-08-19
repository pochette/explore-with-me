package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.RequestMapper;
import ru.burdak.mainservice.model.Request;
import ru.burdak.mainservice.repository.RequestRepository;
import ru.burdak.mainservice.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor

public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<ParticipationRequestDto> getRequestsByUserPrivateController(HttpServletRequest httpServletRequest,
                                                                                  Long userId) {
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with id=" + userId +
                " was not found"));

        List<Request> request = requestRepository.findAllByRequester_Id(userId);
        return request
            .stream()
            .map(RequestMapper::toDto)
            .toList();
    }
}
