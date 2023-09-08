package com.company.youse.errorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnknownIPAddressException extends Throwable {
    public UnknownIPAddressException(String ipAddress) {
        super("Received transaction from an untrusted source IP: " + ipAddress);
    }
}