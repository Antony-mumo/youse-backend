package com.company.youse.errorHandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler({
            BadRequestException.class,
    })
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp",  new java.util.Date());
        errorResponse.put("message", e.getLocalizedMessage());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("status", 400);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({
            MissingException.class,
    })
    public ResponseEntity<Map<String, Object>> handleMissingException(MissingException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp",  new java.util.Date());
        errorResponse.put("message", e.getLocalizedMessage());
        errorResponse.put("error", "Not Found");
        errorResponse.put("status", 404);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({
            ConflictException.class,
    })
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp",  new java.util.Date());
        errorResponse.put("message", e.getLocalizedMessage());
        errorResponse.put("error", "Conflict");
        errorResponse.put("status", 409);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }



    @ExceptionHandler({
            UnauthorizedException.class,
    })
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp",  new java.util.Date());
        errorResponse.put("message", e.getLocalizedMessage());
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("status", 401);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }



    @ExceptionHandler({
            ServerErrorException.class,
    })
    public ResponseEntity<Map<String, Object>> handleServerErrorException(ServerErrorException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp",  new java.util.Date());
        errorResponse.put("message", e.getLocalizedMessage());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("status", 500);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}