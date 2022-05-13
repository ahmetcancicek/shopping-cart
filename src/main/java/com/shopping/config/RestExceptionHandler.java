package com.shopping.config;

import com.shopping.domain.dto.ErrorResponse;
import com.shopping.domain.exception.AlreadyExistsElementException;
import com.shopping.domain.exception.NoSuchElementFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistsElementException.class)
    protected ResponseEntity<ErrorResponse<String>> handleAlreadyExistElementException(HttpServletRequest request,
                                                                                       AlreadyExistsElementException ex) {
        log.error("AlreadyExistElementException {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse<>(HttpStatus.BAD_REQUEST, "AlreadyExistsElementException", ex.getMessage()));
    }

    @ExceptionHandler({NoSuchElementFoundException.class})
    protected ResponseEntity<ErrorResponse<String>> handleNotFoundException(HttpServletRequest request,
                                                                            Exception ex) {
        log.error("NotFoundException {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse<>(HttpStatus.NOT_FOUND, "NotFoundException", ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorResponse<String>> handleConstraintViolation(HttpServletRequest request,
                                                                              ValidationException ex) {
        log.error("ValidationException {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse<>(HttpStatus.BAD_REQUEST, "ValidationException", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse<String>> handleAccessDeniedException(HttpServletRequest request,
                                                                                AccessDeniedException ex) {
        log.error("AccessDeniedException {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse<>(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<String>> handleInternalServerError(HttpServletRequest request,
                                                                              Exception ex) {
        log.error("handleInternalServerError {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError", ex.getMessage()));
    }

}
