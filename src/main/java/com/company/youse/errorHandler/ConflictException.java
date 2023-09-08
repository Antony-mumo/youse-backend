package com.company.youse.errorHandler;

public class ConflictException extends RuntimeException{
    public ConflictException(String errorMessage) {
        super(errorMessage);
    }
}
