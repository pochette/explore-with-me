package ru.burdak.mainservice.exception;

public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(String message) {
        super(message);
    }
}
