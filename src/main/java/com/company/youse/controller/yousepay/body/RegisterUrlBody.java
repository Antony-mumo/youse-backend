package com.company.youse.controller.yousepay.body;

import lombok.Data;

@Data
public class RegisterUrlBody {
    private String confirmationUrl;
    private String validationUrl;
    private String shortCode;
    private String shortCodeType;
    private String responseType;
}

