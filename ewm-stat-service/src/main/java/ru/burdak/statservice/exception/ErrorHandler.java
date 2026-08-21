package ru.burdak.statservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
        MethodArgumentTypeMismatchException.class,
        ConstraintViolationException.class,
        IllegalArgumentException.class,
        MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
            .status(status)
            .body(ApiError.builder()
                .status(status.name())
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .build());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleThrowable(Throwable e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
            .status(status)
            .body(ApiError.builder()
                .status(status.name())
                .reason("Internal server error.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .build());
    }
}
