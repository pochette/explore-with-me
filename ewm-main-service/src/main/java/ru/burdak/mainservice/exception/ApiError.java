package ru.burdak.mainservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Value
public class ApiError {
    String message;
    String reason;
    String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
    List<String> errors;
}
