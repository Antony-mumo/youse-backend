package com.company.youse.platform.api.rest;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.constants.Messages;
import org.springframework.http.HttpStatus;

public class InternalServerFailure implements ResponseStrategy {

    @Override
    public InternalMessage getMessage() {
        return Messages.FAILED;
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

