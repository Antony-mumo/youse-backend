package com.company.youse.platform.api.rest;


import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.constants.Messages;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NoContent implements ResponseStrategy {

    @Override
    public <R> ResponseEntity<Response> getResponse(R r, Result base) {
        return ResponseEntity
                .status(httpStatus())
                .build();
    }

    @Override
    public InternalMessage getMessage() {
        return Messages.NO_CONTENT;
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NO_CONTENT;
    }

}

