package com.company.youse.platform.api;


import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.constants.ErrorResponse;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseStrategy {

    default <R> ResponseEntity<Response> getResponse(R data, Result base) {

        ResponseEntity<Response> re = ResponseEntity
                .status(httpStatus())
                .body(new ErrorResponse(base));

        return re;
    }

    HttpStatus httpStatus();

    InternalMessage getMessage();
}
