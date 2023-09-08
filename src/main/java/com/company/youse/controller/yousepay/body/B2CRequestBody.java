package com.company.youse.controller.yousepay.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class B2CRequestBody {

    //Safaricom B2C Payload
    private String initiator;
    private String commandId;
    private String amount;
    private String partyA;
    private String partyB;
    private String remarks;
    private String queueTimeOutURL;
    private String occasion;

    //B2B Extra Payload
    private Integer senderIdentifierType;
    private Integer receiverIdentifierType;
    private String accountReference;

    private B2CCredentials credentials;
    private String callbackUrl;


    private String tenantId;

    private Boolean isB2B = false;
}
