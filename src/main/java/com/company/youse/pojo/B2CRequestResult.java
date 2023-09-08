package com.company.youse.pojo;

import com.company.youse.interfaces.ClientResponse;
import com.company.youse.models.yousepay.B2CTransaction;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class B2CRequestResult implements ClientResponse, Serializable {

    private String originatorConversationID;
    private String conversationID;
    private String transactionID;

    private String resultCode;
    private String resultType;
    private String resultDescription;

    private BigDecimal transactionAmount;
    private String transactionReceipt;
    private String b2CRecipientIsRegisteredCustomer;
    private BigDecimal b2CChargesPaidAccountAvailableFunds;
    private String receiverPartyPublicName;
    private Date transactionCompletedDateTime;
    private BigDecimal b2CUtilityAccountAvailableFunds;
    private BigDecimal b2CWorkingAccountAvailableFunds;
    private String transactionStatus;
    private String reasonType;
    private String debitPartyName;
    private String creditPartyName;

    private String correlationID;

    public void copyFrom(B2CTransaction b2CTransaction){
        setOriginatorConversationID(b2CTransaction.getOriginatorConversationID());
        setConversationID(b2CTransaction.getConversationID());
        setTransactionID(b2CTransaction.getTransactionID());
        setResultCode(b2CTransaction.getResultCode());
        setResultDescription(b2CTransaction.getResultDescription());
        setTransactionAmount(b2CTransaction.getTransactionAmount());
        setTransactionReceipt(b2CTransaction.getTransactionReceipt());
        setB2CRecipientIsRegisteredCustomer(b2CTransaction.getB2CRecipientIsRegisteredCustomer());
        setB2CChargesPaidAccountAvailableFunds(b2CTransaction.getB2CChargesPaidAccountAvailableFunds());
        setReceiverPartyPublicName(b2CTransaction.getReceiverPartyPublicName());
        setResultType(b2CTransaction.getResultType());
        setTransactionCompletedDateTime(b2CTransaction.getTransactionCompletedDateTime());
        setB2CUtilityAccountAvailableFunds(b2CTransaction.getB2CUtilityAccountAvailableFunds());
        setB2CWorkingAccountAvailableFunds(b2CTransaction.getB2CWorkingAccountAvailableFunds());
        setTransactionStatus(b2CTransaction.getTransactionStatus());
        setReasonType(b2CTransaction.getReasonType());
        setDebitPartyName(b2CTransaction.getDebitPartyName());
        setCreditPartyName(b2CTransaction.getCreditPartyName());
        setCorrelationID(b2CTransaction.getB2CRequest().getCorrelationId());
    }
}
