package com.inventory.backend.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation errors: {}", errors);

        return new ResponseEntity<>(
                new ErrorResponse("validation_error", "Validation failed", "ValidationException", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String rawMessage = extractRootCauseMessage(ex);
        log.error("Data integrity violation: {}", rawMessage);

        String field = extractFieldFromConstraint(rawMessage);
        String friendlyMessage = generateFriendlyMessage(rawMessage, field);

        Map<String, String> details = new HashMap<>();
        if (field != null) {
            details.put("field", field);
        }
        details.put("originalMessage", rawMessage);

        return new ResponseEntity<>(
                new ErrorResponse("data_integrity_error", friendlyMessage,
                        "DataIntegrityViolationException", details),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("exceptionClass", ex.getClass().getSimpleName());
        details.put("stackTrace", getFirstFewStackTraceLines(ex));

        return new ResponseEntity<>(
                new ErrorResponse("unexpected_error",
                        "An unexpected error occurred: " + ex.getMessage(),
                        "UnexpectedException",
                        details),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException ex) {
        log.error("SQL error: {}", ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("sqlState", ex.getSQLState());
        details.put("errorCode", String.valueOf(ex.getErrorCode()));

        return new ResponseEntity<>(
                new ErrorResponse("sql_error",
                        "A database error occurred: " + ex.getMessage(),
                        "SQLException",
                        details),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private String extractRootCauseMessage(Throwable ex) {
        Throwable rootCause = ex;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause.getMessage();
    }

    private String extractFieldFromConstraint(String rawMessage) {
        if (rawMessage == null) {
            return null;
        }

        Pattern[] patterns = {
                Pattern.compile("for key '(.+?)'"),
                Pattern.compile("column (.+?) cannot have null"),
                Pattern.compile("Duplicate entry '.+?' for key '(.+?)'")
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(rawMessage);
            if (matcher.find()) {
                String key = matcher.group(1);
                log.info("Constraint Key Detected: {}", key);

                if (key.contains("UK1dg427qt898j7shqnvv5s617p")) {
                    return "id (UUID)";
                }
                if (key.toLowerCase().contains("email")) {
                    return "email";
                }
                return key;
            }
        }
        return null;
    }

    private String generateFriendlyMessage(String rawMessage, String field) {
        if (rawMessage == null) {
            return "A data integrity error occurred.";
        }

        rawMessage = rawMessage.toLowerCase();

        if (rawMessage.contains("duplicate") || rawMessage.contains("unique")) {
            return field != null
                    ? "The " + field + " already exists and must be unique."
                    : "A duplicate value was detected.";
        }

        if (rawMessage.contains("null") || rawMessage.contains("cannot be null")) {
            return field != null
                    ? "The " + field + " is required and cannot be empty."
                    : "A required field was left empty.";
        }

        return "A data integrity violation occurred: " + rawMessage;
    }

    private String getFirstFewStackTraceLines(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(3, stackTrace.length); i++) {
            sb.append(stackTrace[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
