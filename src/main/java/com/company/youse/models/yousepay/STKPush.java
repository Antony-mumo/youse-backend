package com.company.youse.models.yousepay;

import com.company.youse.models.PO;
import com.company.youse.pojo.STKPushRequest;
import com.company.youse.pojo.STKPushRequestResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "stk_push")
public class STKPush extends PO {

    private String transactionType;
    private String amount;
    private String partyB;
    private String partyA;
    private String phoneNumber;
    private String accountReference;
    private String transactionDesc;

    private String merchantRequestId;

    private String checkoutRequestId;
    private String responseDescription;
    private String customerMessage;

    private String receiptNumber;

    private Boolean isAccepted;
    private Boolean isCompleted;

    private String resultDescription;

    public void copyFrom(STKPushRequest stkPushRequest) {
        this.transactionType = stkPushRequest.getTransactionType();
        this.amount = stkPushRequest.getAmount();
        this.partyB = stkPushRequest.getPartyB();
        this.partyA = stkPushRequest.getPartyA();
        this.phoneNumber =  stkPushRequest.getPhoneNumber();
        this.accountReference = stkPushRequest .getAccountReference();
        this.transactionDesc = stkPushRequest.getTransactionDesc();
    }

    public void copyFrom(STKPushRequestResponseBody stkResponse) {
        this.isAccepted = stkResponse.isAccepted();
        this.transactionDesc = stkResponse.getDescription();
        this.merchantRequestId = stkResponse.getMerchantRequestId();
        this.checkoutRequestId = stkResponse.getCheckoutRequestId();
        this.customerMessage = stkResponse.getCustomerMessage();
    }
}
