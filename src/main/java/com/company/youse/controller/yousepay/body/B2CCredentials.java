package com.company.youse.controller.yousepay.body;

import lombok.Data;

@Data
public class B2CCredentials {
    //Third Party Required Payload
    private String shortCode;
    private String mpesaAppKey;
    private String mpesaAppSecret;
    private String correlationId;
    private String securityCredential;
}
