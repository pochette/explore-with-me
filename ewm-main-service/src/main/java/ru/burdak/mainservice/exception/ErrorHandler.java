package ru.burdak.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<ApiError> conditionsNotMetExceptionHandler(Exception e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
            ApiError
                .builder()
                .message(e.getMessage())
                .status(httpStatus.name())
                .reason(e.getCause() != null ? e
                    .getCause()
                    .toString() : "For the requested operation the conditions are not met.")
                .timestamp(LocalDateTime.now())
                .errors(Arrays
                    .stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .build(),
            httpStatus);
    }

    @ExceptionHandler({
        ConflictException.class
    })
    public ResponseEntity<ApiError> conflictExceptionHandler(Exception e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
            ApiError
                .builder()
                .message(e.getMessage())
                .status(httpStatus.name())
                .reason(e.getCause() != null ? e
                    .getCause()
                    .toString() : "Integrity constraint has been violated.")
                .timestamp(LocalDateTime.now())
                .errors(Arrays
                    .stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .build(),
            httpStatus);
    }



    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
        BadRequestException.class
    })
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> "Field: " + error.getField()
                + ". Error: " + error.getDefaultMessage()
                + ". Value: " + error.getRejectedValue())
            .findFirst()
            .orElse(e.getMessage());
        return new ResponseEntity<>(
            ApiError
                .builder()
                .message(message)
                .errors(Arrays
                    .stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .reason(e.getCause() != null ? e
                    .getCause()
                    .toString() : "Incorrectly made request.")
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.name())
                .build(),

            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFoundExceptionHandler(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
            ApiError
                .builder()
                .errors(Arrays
                    .stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .message(e.getMessage())
                .reason(e.getCause() != null ? e
                    .getCause()
                    .toString() : "The required object was not found.")
                .timestamp(LocalDateTime.now())
                .status(httpStatus.name())
                .build(),
            httpStatus);
    }
}





