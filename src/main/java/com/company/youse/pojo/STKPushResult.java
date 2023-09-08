package com.company.youse.pojo;

import com.company.youse.models.yousepay.STKPush;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class STKPushResult {

    private String merchantRequestId;
    private String checkoutRequestId;
    private String resultDescription;
    private int resultCode;

    private String receiptNumber;

    private String shortCode;
    private String phoneNumber;
    private Date transactionDate;

    private Boolean isCompleted;

    public void copyFrom(STKPush stkPush) {
        this.merchantRequestId = stkPush.getMerchantRequestId();
        this.checkoutRequestId = stkPush.getCheckoutRequestId();
        this.resultDescription = stkPush.getResultDescription();
        this.shortCode = stkPush.getPartyB();
        this.phoneNumber = stkPush.getPartyB();
        this.isCompleted = stkPush.getIsCompleted();
    }
}
