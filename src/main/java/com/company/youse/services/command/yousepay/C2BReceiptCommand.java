package com.company.youse.services.command.yousepay;


import com.company.youse.controller.yousepay.body.C2BTransactionBody;
import com.company.youse.platform.services.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class C2BReceiptCommand implements Command {

    private String transactionType;
    private String transactionId;
    private Date transactionTime;
    private Double transactionAmount;
    private String businessShortCode;
    private String billRefNo;
    private Double orgAccountBalance;
    private String msisdn;
    private String firstName;
    private String lastName;
    private String middleName;
    private String conversationId;
    private String overrideIPPass;

    @JsonIgnore
    private String ipAddress;

    @JsonIgnore
    private Boolean manualPosting;

    public C2BReceiptCommand(C2BTransactionBody body) {
        this.transactionType = body.getTransactionType();
        this.transactionId = body.getTransactionId();
        this.transactionTime = body.getTransactionTime();
        this.transactionAmount =  body.getTransactionAmount();
        this.businessShortCode = body.getBusinessShortCode();
        this.billRefNo = body.getBillRefNo();
        this.orgAccountBalance = body.getOrgAccountBalance();
        this.msisdn = body.getMsisdn();
        this.firstName = body.getFirstName();
        this.lastName = body.getLastName();
        this.middleName = body.getMiddleName();
        this.overrideIPPass = body.getOverrideIPPass();
        this.conversationId = body.getConversationId();
    }
}

