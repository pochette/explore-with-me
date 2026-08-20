package ru.burdak.mainservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        BadRequestException.class
    })

    public ResponseEntity<ApiError> badRequestExceptionHandler(Exception e) {
        return buildBadRequest(e, e.getMessage());
    }


    @ResponseStatus(HttpStatus.CONFLICT)
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

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> "Field: " + error.getField()
                + ". Error: " + error.getDefaultMessage()
                + ". Value: " + error.getRejectedValue())
            .findFirst()
            .orElse(e.getMessage());

        return buildBadRequest(e, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
            .stream()
            .map(violation -> "Field: " + violation.getPropertyPath()
                + ". Error: " + violation.getMessage()
                + ". Value: " + violation.getInvalidValue())
            .findFirst()
            .orElse(e.getMessage());

        return buildBadRequest(e, message);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
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

    private ResponseEntity<ApiError> buildBadRequest(Exception e, String message) {
        return new ResponseEntity<>(
            ApiError.builder()
                .message(message)
                .reason("Incorrectly made request.")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList())
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }
}





