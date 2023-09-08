package com.company.youse.platform.constants;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.contract.ResponseMessage;
import com.company.youse.platform.result.Result;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse implements Response {

    private List<ResponseMessage> errors;

    public ErrorResponse(Result result) {
        super();
        this.errors = result.toMessages();
    }

    public ErrorResponse(InternalMessage message) {
        super();
        this.errors = new ArrayList<>();
        this.errors.add(message == null ? Messages.FAILED.toMessage() : message.toMessage());
    }

}

