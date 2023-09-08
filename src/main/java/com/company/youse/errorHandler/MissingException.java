package com.company.youse.errorHandler;

public class MissingException extends RuntimeException{
    public MissingException(String errorMessage) {
        super(errorMessage);
    }
}

