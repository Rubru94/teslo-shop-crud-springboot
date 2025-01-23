package com.teslo.teslo_shop.core.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.teslo.teslo_shop.core.error.exceptions.BadRequestException;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.core.error.exceptions.UnauthorizedException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception ex, WebRequest request) {
        return this.handleException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(Exception ex, WebRequest request) {
        return this.handleException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(Exception ex, WebRequest request) {
        return this.handleException(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return this.handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> handleException(Exception ex, WebRequest request, HttpStatusCode status) {
        ErrorDetailRecord errorDetail = new ErrorDetailRecord(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, status);
    }
}
