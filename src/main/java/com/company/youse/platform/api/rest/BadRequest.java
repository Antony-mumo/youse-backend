package com.company.youse.platform.api.rest;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.contract.Type;
import org.springframework.http.HttpStatus;

public class BadRequest implements ResponseStrategy {

    @Override
    public InternalMessage getMessage() {
        return InternalMessage.builder()
                .type(Type.FAILURE)
                .code("validation_failure")
                .message("Sorry this seems to be an invalid request")
                .build();
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
