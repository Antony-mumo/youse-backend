package com.company.youse.services.command.yousepay;


import com.company.youse.controller.yousepay.body.B2CRequestBody;
import com.company.youse.platform.services.Command;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
public class B2CRequestCommand implements Command, Serializable {

    private String initiator;
    private String securityCredential;
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

    //Third Party Required Payload
    private String shortCode;
    private String mpesaAppKey;
    private String mpesaAppSecret;
    private String correlationId;
    private String callbackUrl;

    private String tenantId;

    private Boolean isB2B = false;

    public B2CRequestCommand(B2CRequestBody request) {
        this.initiator = request.getInitiator();
        this.securityCredential = request.getCredentials().getSecurityCredential();
        this.commandId = request.getCommandId();
        this.amount = request.getAmount();
        this.partyA = request.getPartyA();
        this.partyB = request.getPartyB();
        this.remarks = request.getRemarks();
        this.queueTimeOutURL =  request.getQueueTimeOutURL();
        this.occasion = request.getOccasion();

        this.senderIdentifierType = request.getSenderIdentifierType();
        this.receiverIdentifierType = request.getReceiverIdentifierType();
        this.accountReference = request.getAccountReference();

        this.shortCode = request.getCredentials().getShortCode();
        this.mpesaAppKey = request.getCredentials().getMpesaAppKey();
        this.mpesaAppSecret = request.getCredentials().getMpesaAppSecret();
        this.correlationId = request.getCredentials().getCorrelationId();
        this.callbackUrl = request.getCallbackUrl();

        this.tenantId = request.getTenantId();
        this.isB2B = request.getIsB2B();
    }
}

