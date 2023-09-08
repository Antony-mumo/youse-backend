package com.company.youse.pojo;

import com.company.youse.interfaces.MpesaRequest;
import com.company.youse.services.query.yousepay.GetB2CTransactionQuery;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2CTransactionQueryRequest implements MpesaRequest {

    //Request
    @JsonProperty("Initiator")
    private String initiator;
    @JsonProperty("SecurityCredential")
    private String securityCredential;
    @JsonProperty("CommandID")
    private String commandID;
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("PartyA")
    private String partyA;
    @JsonProperty("IdentifierType")
    private String identifierType;
    @JsonProperty("ResultURL")
    private String resultURL;
    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;
    @JsonProperty("Remarks")
    private String remarks;
    @JsonProperty("Occasion")
    private String occasion;

    public void copyFrom(GetB2CTransactionQuery query) {
        setInitiator(query.getInitiator());
        setSecurityCredential(query.getSecurityCredential());
        setTransactionID(query.getTransactionId());
        setPartyA(query.getAccountNumber());
        setIdentifierType(query.getIdentifierType());
        setOccasion(query.getOccasion());
        setRemarks(query.getRemarks());
    }
}
