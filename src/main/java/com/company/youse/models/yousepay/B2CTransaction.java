package com.company.youse.models.yousepay;

import com.company.youse.models.PO;
import com.company.youse.pojo.B2CRequestResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "b2c_transactions")
public class B2CTransaction extends PO {

    private String originatorConversationID;
    private String conversationID;
    private String resultType;
    private String resultCode;
    private String resultDescription;
    private String transactionID;
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

    @OneToOne
    @JoinColumn(name = "b2c_request_id")
    private B2CRequest b2CRequest;

    @OneToOne
    @JoinColumn(name = "b2c_transaction_query_id")
    private B2CTransactionQuery b2CTransactionQuery;

    private boolean completed;
    private String cause;
    private String tenantId;

    public void copyFrom(B2CRequestResult result) {
        setOriginatorConversationID(result.getOriginatorConversationID());
        setConversationID(result.getConversationID());
        setTransactionID(result.getTransactionID());
        setResultCode(result.getResultCode());
        setResultType(result.getResultType());
        setResultDescription(result.getResultDescription());
        setTransactionAmount(result.getTransactionAmount());
        setTransactionReceipt(result.getTransactionReceipt());
        setB2CRecipientIsRegisteredCustomer(result.getB2CRecipientIsRegisteredCustomer());
        setB2CChargesPaidAccountAvailableFunds(result.getB2CChargesPaidAccountAvailableFunds());
        setReceiverPartyPublicName(result.getReceiverPartyPublicName());
        setTransactionCompletedDateTime(result.getTransactionCompletedDateTime());
        setB2CUtilityAccountAvailableFunds(result.getB2CUtilityAccountAvailableFunds());
        setB2CWorkingAccountAvailableFunds(result.getB2CWorkingAccountAvailableFunds());
        setTransactionStatus(result.getTransactionStatus());
        setReasonType(result.getReasonType());
        setDebitPartyName(result.getDebitPartyName());
        setCreditPartyName(result.getCreditPartyName());
    }
}
