package com.company.youse.pojo;

import com.company.youse.interfaces.MpesaRequest;
import com.company.youse.services.command.yousepay.STKPushRequestCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class STKPushRequest implements MpesaRequest {

    @JsonProperty("Password")
    private String password;
    @JsonProperty("Timestamp")
    private String timestamp;
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("PartyB")
    private String partyB;
    @JsonProperty("PartyA")
    private String partyA;
    @JsonProperty("PhoneNumber")
    private String phoneNumber;
    @JsonProperty("CallBackURL")
    private String callBackURL;
    @JsonProperty("AccountReference")
    private String accountReference;
    @JsonProperty("TransactionDesc")
    private String transactionDesc;

    public STKPushRequest(STKPushRequestCommand command) {
        this.transactionType = command.getTransactionType();
        this.amount = command.getAmount();
        this.partyB = command.getPartyB();
        this.partyA = command.getPartyA();
        this.phoneNumber =  command.getPhoneNumber();
        this.accountReference = command .getAccountReference();
        this.transactionDesc = command.getTransactionDesc();
    }
}
