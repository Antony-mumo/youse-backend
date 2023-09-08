package com.company.youse.controller.yousepay.body;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class C2BTransactionBody {

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonProperty("TransID")
    private String transactionId;

    @JsonProperty("TransTime")
    private Date transactionTime;

    @JsonProperty("TransAmount")
    private Double transactionAmount;

    @JsonProperty("BusinessShortCode")
    private String businessShortCode;

    @JsonProperty("BillRefNumber")
    private String billRefNo;

    @JsonProperty("OrgAccountBalance")
    private Double orgAccountBalance;

    @JsonProperty("MSISDN")
    private String msisdn;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("ConversationID")
    private String conversationId;

    @JsonProperty("OverrideIpPass")
    private String overrideIPPass;
}
