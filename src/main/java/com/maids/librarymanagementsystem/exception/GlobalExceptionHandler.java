package com.maids.librarymanagementsystem.exception;

import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse>handleResourceNotFoundException(ResourceNotFoundException exception){
        CustomErrorResponse errorResponse=new CustomErrorResponse(HttpStatus.NOT_FOUND.value(),exception.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    // ConflictException
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomErrorResponse>handleResourceNotFoundException(ConflictException exception){
        CustomErrorResponse errorResponse=new CustomErrorResponse(HttpStatus.CONFLICT.value(),exception.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    // BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorResponse>handleResourceNotFoundException(BadRequestException exception){
        CustomErrorResponse errorResponse=new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(),exception.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value()," ex.getMessage()");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // General Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse>handleResourceNotFoundException(Exception exception){
        CustomErrorResponse errorResponse=new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
