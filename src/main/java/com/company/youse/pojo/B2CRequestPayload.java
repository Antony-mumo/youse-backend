package com.company.youse.pojo;

import com.company.youse.interfaces.MpesaRequest;
import com.company.youse.services.command.yousepay.B2CRequestCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class B2CRequestPayload implements MpesaRequest {

    @JsonProperty("InitiatorName")
    private String initiator;
    @JsonProperty("SecurityCredential")
    private String securityCredential;
    @JsonProperty("CommandID")
    private String commandId;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("PartyA")
    private String partyA;
    @JsonProperty("PartyB")
    private String partyB;
    @JsonProperty("Remarks")
    private String remarks;
    @JsonProperty("ResultURL")
    private String resultURL;
    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;
    @JsonProperty("Occassion")
    private String occasion;
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    public B2CRequestPayload(B2CRequestCommand command) {
        this.initiator = command.getInitiator();
        this.securityCredential = command.getSecurityCredential();
        this.commandId = command.getCommandId();
        this.amount = command.getAmount();
        this.partyA = command.getPartyA();
        this.partyB = command.getPartyB();
        this.remarks = command.getRemarks();
        this.queueTimeOutURL =  command.getQueueTimeOutURL();
        this.originatorConversationID = command.getCorrelationId();
    }
}
