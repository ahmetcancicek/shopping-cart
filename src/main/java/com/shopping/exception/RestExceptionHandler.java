package com.shopping.exception;

import com.shopping.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        // TODO: Add message
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler({UserNotFoundException.class, ProductNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception ex) {
        // TODO: Add message

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        // TODO: Add constraint validation errors list
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation error")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(ErrorResponse.builder().build());
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
