package com.company.youse.services.command.yousepay;

import com.company.youse.controller.yousepay.body.RegisterUrlBody;
import com.company.youse.platform.services.Command;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterUrlCommand implements Command {

    @JsonProperty("ConfirmationURL")
    private String confirmationUrl;
    @JsonProperty("ValidationURL")
    private String validationUrl;
    @JsonProperty("ShortCode")
    private String shortCode;
    @JsonProperty("ResponseType")
    private String responseType;

    public RegisterUrlCommand(RegisterUrlBody body){
        this.confirmationUrl = body.getConfirmationUrl();
        this.validationUrl = body.getValidationUrl();
        this.shortCode = body.getShortCode();
        this.responseType = body.getResponseType();
    }
}

