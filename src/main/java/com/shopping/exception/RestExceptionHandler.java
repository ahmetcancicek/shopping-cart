package com.shopping.exception;

import com.shopping.dto.ErrorMessage;
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
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorMessage);
    }

    @ExceptionHandler({UserNotFoundException.class, ProductNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception ex) {
        // TODO: Add message

        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorMessage);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        // TODO: Add constraint validation errors list
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("Validation error")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(ErrorMessage.builder().build());
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorMessage errorMessage) {
        return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
    }
}
