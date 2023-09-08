package com.company.youse.errorHandler;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
