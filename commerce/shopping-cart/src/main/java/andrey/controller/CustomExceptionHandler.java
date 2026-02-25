package andrey.controller;

import andrey.exception.ValidationException;
import andrey.exception.base.BaseAbstractException;
import andrey.exception.base.BaseGlobalExceptionHandler;
import andrey.exception.cart.NotAuthorizedUserException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends BaseGlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseAbstractException> handleConstraint(ConstraintViolationException ex) {
        // 1. Ищем нарушение для username (приоритет 401)
        var usernameViolation = ex.getConstraintViolations().stream()
                .filter(v -> v.getPropertyPath().toString().endsWith("username"))
                .findFirst();

        if (usernameViolation.isPresent()) {
            // Выплевываем NotAuthorizedUserException (401)
            var authEx = new NotAuthorizedUserException(
                    ex.getMessage(),
                    usernameViolation.get().getMessage()
            );
            return new ResponseEntity<>(authEx, authEx.getHttpStatus());
        }

        // 2. Для всего остального выплевываем ValidationException (400)
        String details = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        var validationEx = new ValidationException(
                ex.getMessage(),
                "Ошибка валидации: " + details
        );

        return new ResponseEntity<>(validationEx, validationEx.getHttpStatus());
    }
}

