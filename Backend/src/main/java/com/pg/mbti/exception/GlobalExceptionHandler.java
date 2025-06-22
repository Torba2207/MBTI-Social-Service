package com.pg.mbti.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 BAD REQUEST
    @ExceptionHandler({
            InvalidTokenException.class,
            InvalidEmailException.class,
            EmailNotConfirmedException.class,
            ValidationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<String> handleBadRequestExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 401 UNAUTHORIZED
    @ExceptionHandler({
            InvalidPasswordException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<String> handleUnauthorizedExceptions(Exception ex) {
        return new ResponseEntity<>("Invalid credentials: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 404 NOT FOUND
    @ExceptionHandler({
            ResourceNotFoundException.class,
            FileNotFoundException.class,
            FriendshipNotFoundException.class,
            QuestionNotFoundException.class,
            TagCategoryNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 409 CONFLICT
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            FriendshipAlreadyExistsException.class
    })
    public ResponseEntity<String> handleConflictExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // 410 GONE
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpired(TokenExpiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.GONE);
    }

    // 500 INTERNAL SERVER ERROR
    @ExceptionHandler({
            EmailSendingFailedException.class,
            FileUploadException.class
    })
    public ResponseEntity<String> handleServerErrors(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // FALLBACK: SPRING ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    // GENERIC fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
