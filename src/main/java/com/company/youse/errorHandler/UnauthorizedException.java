package com.company.youse.errorHandler;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
