package com.shopping.exception;

import com.shopping.dto.ErrorRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistsElementException.class)
    protected ResponseEntity<Object> handleAlreadyExistElementException(AlreadyExistsElementException ex) {
        // TODO: Add message
        ErrorRequest errorRequest = ErrorRequest.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorRequest);
    }

    @ExceptionHandler({NoSuchElementFoundException.class, ProductNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception ex) {
        // TODO: Add message

        ErrorRequest errorRequest = ErrorRequest.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorRequest);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        // TODO: Add constraint validation errors list
        ErrorRequest errorRequest = ErrorRequest.builder()
                .message("Validation error")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(ErrorRequest.builder().build());
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorRequest errorRequest) {
        return new ResponseEntity<>(errorRequest, errorRequest.getStatus());
    }
}
