package ru.burdak.mainservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.burdak.mainservice.model.RequestStatus;

import java.time.LocalDateTime;

/**
 *
 * @param created Дата и время создания заявки
 * @param event Идентификатор события
 * @param id Идентификатор заявки
 * @param requester Идентификатор пользователя, отправившего заявку
 * @param status Статус заявки
 */
public record ParticipationRequestDto(

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created,

    Long event,

    Long id,

    Long requester,

    RequestStatus status

) {
}
