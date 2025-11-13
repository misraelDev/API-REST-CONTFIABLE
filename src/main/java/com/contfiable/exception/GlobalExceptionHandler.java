package com.contfiable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.info("Validation error occurred: {}", ex.getMessage());

        List<ApiError> errorItems = new java.util.ArrayList<>();
        List<String> details = new java.util.ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            logger.info("Validation error - Field: {}, Message: {}", fieldName, errorMessage);
            errorItems.add(new ApiError(errorMessage, fieldName));
            details.add("%s: %s".formatted(fieldName, errorMessage));
        });

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Los datos proporcionados no son válidos",
                errorItems,
                details
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.info("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                List.of(new ApiError(ex.getMessage()))
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        logger.info("Bad request: {}", ex.getMessage());
        
        // Errores de autenticación deben devolver 401
        String message = ex.getMessage();
        if (message != null && (message.contains("correo electrónico no está registrado") 
                || message.contains("contraseña es incorrecta")
                || message.contains("credenciales"))) {
            return buildErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    message,
                    List.of(new ApiError(message))
            );
        }
        
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                List.of(new ApiError(message))
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        logger.info("Business exception: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage(),
                List.of(new ApiError(ex.getMessage()))
        );
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        logger.info("Data integrity violation: {}", ex.getMessage());

        String message = "Error de integridad de datos";
        if (ex.getMessage() != null) {
            String normalized = ex.getMessage().toLowerCase();
            if (normalized.contains("email") || normalized.contains("correo")) {
                message = "El email ya está registrado en el sistema";
            } else if (normalized.contains("phone") || normalized.contains("teléfono")) {
                message = "El teléfono ya está registrado en el sistema";
            }
        }

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                message,
                List.of(new ApiError(message))
        );
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex) {
        logger.info("Constraint violation: {}", ex.getMessage());

        List<ApiError> errorItems = new java.util.ArrayList<>();
        List<String> details = new java.util.ArrayList<>();

        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            logger.info("Constraint violation - Field: {}, Message: {}", field, message);
            errorItems.add(new ApiError(message, field));
            details.add("%s: %s".formatted(field, message));
        });

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Los datos proporcionados no son válidos",
                errorItems,
                details
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime exception occurred: ", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "Error de ejecución";
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message,
                List.of(new ApiError(message)),
                ex.getClass().getSimpleName()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "Ha ocurrido un error interno del servidor";
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message,
                List.of(new ApiError(message)),
                ex.getClass().getSimpleName()
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            List<ApiError> errors
    ) {
        return buildErrorResponse(status, message, errors, null);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            List<ApiError> errors,
            Object details
    ) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value());
        response.setMessage(message);
        response.setError(status.getReasonPhrase());
        response.setErrors(errors != null && !errors.isEmpty() ? errors : List.of(new ApiError(message)));
        if (details != null) {
            response.setDetails(details);
        }
        return ResponseEntity.status(status).body(response);
    }
}
