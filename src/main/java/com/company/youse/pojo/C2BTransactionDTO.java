package com.company.youse.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class C2BTransactionDTO {
    private Long id;
    private String transactionType;
    private String transactionId;
    private Date transactionTime;
    private Double transactionAmount;
    private String businessShortCode;
    private String billRefNo;
    private Boolean isReversed = false;
    private Double orgAccountBalance;
    private String msisdn;
    private String firstName;
    private String lastName;
    private String middleName;
    private String ipAddress;
    private String OriginatorConversationID;
    private String ConversationID;
    private String ResponseDescription;
    private String ResultDesc;

    private boolean isSynced;
    private Date dateSynced;
    private String syncMessage;
}

