package com.company.youse.platform.api.rest;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.constants.Messages;
import org.springframework.http.HttpStatus;

public class NotFound implements ResponseStrategy {

    @Override
    public InternalMessage getMessage() {
        return Messages.NOT_FOUND;
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
