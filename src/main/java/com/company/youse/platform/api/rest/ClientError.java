package com.company.youse.platform.api.rest;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.constants.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class ClientError implements ResponseStrategy {

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public InternalMessage getMessage() {
        return Messages.FAILED.toBuilder()
                .httpStatus(httpStatus.value())
                .message(message)
                .build();
    }

}
