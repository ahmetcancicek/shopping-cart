package com.shopping.exception;

import com.shopping.dto.ApiResponse;
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
        ApiResponse errorResponse = ApiResponse.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler({NoSuchElementFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception ex) {
        // TODO: Add message

        ApiResponse errorResponse = ApiResponse.builder()
                .message("")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        // TODO: Add constraint validation errors list
        ApiResponse errorResponse = ApiResponse.builder()
                .message("Validation error")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return buildResponseEntity(ApiResponse.builder().build());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
