package com.company.youse.models.yousepay;

import com.company.youse.models.PO;
import com.company.youse.services.command.yousepay.B2CRequestCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "b2c_requests")
public class B2CRequest extends PO{

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

    //Accepted for processing
    private Boolean isAccepted;
    private String errorCause;

    //For Mapping Transaction From Safaricom

    private String originatorConversationId;

    public B2CRequest(B2CRequestCommand body){
        this.initiator = body.getInitiator();
        this.securityCredential = body.getSecurityCredential();
        this.commandId = body.getCommandId();
        this.amount = body.getAmount();
        this.partyA = body.getPartyA();
        this.partyB = body.getPartyB();
        this.remarks = body.getRemarks();
        this.queueTimeOutURL =  body.getQueueTimeOutURL();
        this.occasion = body.getOccasion();

        this.senderIdentifierType = body.getSenderIdentifierType();
        this.receiverIdentifierType = body.getReceiverIdentifierType();
        this.accountReference = body.getAccountReference();

        this.shortCode = body.getShortCode();
        this.mpesaAppKey = body.getMpesaAppKey();
        this.correlationId = body.getCorrelationId();
        this.callbackUrl = body.getCallbackUrl();

        this.tenantId = body.getTenantId();
    }
}
