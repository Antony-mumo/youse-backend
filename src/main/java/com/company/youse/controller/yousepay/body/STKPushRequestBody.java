package com.company.youse.controller.yousepay.body;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class STKPushRequestBody {

    private String transactionType;
    private String amount;
    private String partyB;
    private String partyA;
    private String shortCodeType;
    private String phoneNumber;
    private String accountReference;
    private String transactionDesc;
}
