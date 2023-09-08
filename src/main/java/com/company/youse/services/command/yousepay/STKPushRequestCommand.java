package com.company.youse.services.command.yousepay;


import com.company.youse.controller.yousepay.body.STKPushRequestBody;
import com.company.youse.platform.services.Command;
import lombok.Data;

@Data
public class STKPushRequestCommand implements Command {

    private String transactionType;
    private String amount;
    private String partyB;
    private String partyA;
    private String phoneNumber;
    private String accountReference;
    private String transactionDesc;

    public STKPushRequestCommand(STKPushRequestBody stkPushRequestBody) {
        this.transactionType = stkPushRequestBody.getTransactionType();
        this.amount = stkPushRequestBody.getAmount();
        this.partyB = stkPushRequestBody.getPartyB();
        this.partyA = stkPushRequestBody.getPartyA();
        this.phoneNumber =  stkPushRequestBody.getPhoneNumber();
        this.accountReference = stkPushRequestBody .getAccountReference();
        this.transactionDesc = stkPushRequestBody.getTransactionDesc();
    }
}

