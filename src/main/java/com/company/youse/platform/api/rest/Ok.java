package com.company.youse.platform.api.rest;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.constants.Messages;
import com.company.youse.platform.contract.BodyResponse;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Ok implements ResponseStrategy {

    @Override
    public <R> ResponseEntity<Response> getResponse(R data, Result base) {
        return ResponseEntity
                .status(httpStatus())
                .body(new BodyResponse<R>(data, base));
    }

    @Override
    public InternalMessage getMessage() {
        return Messages.SUCCESS;
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.OK;
    }
}
