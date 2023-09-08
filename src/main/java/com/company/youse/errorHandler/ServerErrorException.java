package com.company.youse.errorHandler;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException(String errorMessage) {
        super(errorMessage);
    }
}

