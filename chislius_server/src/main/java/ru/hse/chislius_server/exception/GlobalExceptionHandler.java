package ru.hse.chislius_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(DataValidationException.class)
    public ErrorResponse handleDataValidationException(DataValidationException ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ErrorResponse handleAuthorizationException(AuthorizationException ex) {
        return ErrorResponse.create(ex, HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(GenerationTimeoutException.class)
    public ErrorResponse handleGenerationTimeoutException(GenerationTimeoutException ex) {
        return ErrorResponse.create(ex, HttpStatus.REQUEST_TIMEOUT, "Повторите попытку еще раз");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex) {
        return ErrorResponse.create(ex, HttpStatus.NOT_FOUND, "Адрес запроса не найден");
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex) {
        log.error("Unexpected error", ex);
        return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
    }
}