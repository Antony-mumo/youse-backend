package com.company.youse.models.yousepay;

import com.company.youse.models.PO;
import com.company.youse.platform.data.Transferable;
import com.company.youse.pojo.C2BTransactionDTO;
import com.company.youse.services.command.yousepay.C2BReceiptCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "c2b_transactions")
public class C2BTransaction extends PO implements Transferable<C2BTransactionDTO> {

    private String transactionType;
    @Column(unique = true)
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

    private Boolean isManualPosting;

    private String tenantId;

    private boolean isSynced;
    private Date dateSynced;
    private String syncMessage;

    @Override
    public C2BTransactionDTO toDTO() {

        return C2BTransactionDTO.builder()
                .id(getId())
                .transactionType(getTransactionType())
                .billRefNo(getBillRefNo())
                .businessShortCode(getBusinessShortCode())
                .ConversationID(getConversationID())
                .dateSynced(getDateSynced())
                .firstName(getFirstName())
                .ipAddress(getIpAddress())
                .isReversed(getIsReversed())
                .lastName(getLastName())
                .middleName(getMiddleName())
                .msisdn(getMsisdn())
                .orgAccountBalance(getOrgAccountBalance())
                .OriginatorConversationID(getOriginatorConversationID())
                .ResponseDescription(getResponseDescription())
                .ResultDesc(getResultDesc())
                .syncMessage(getSyncMessage())
                .transactionId(getTransactionId())
                .transactionTime(getTransactionTime())
                .transactionAmount(getTransactionAmount())
                .isSynced(isSynced())
                .build();
    }

    public void copy(C2BReceiptCommand command) {
        setBillRefNo(command.getBillRefNo());
        setBusinessShortCode(command.getBusinessShortCode());
        setConversationID(command.getConversationId());
        setFirstName(command.getFirstName());
        setLastName(command.getLastName());
        setBillRefNo(command.getBillRefNo());
        setIpAddress(command.getIpAddress());
        setMiddleName(command.getMiddleName());
        setOrgAccountBalance(command.getOrgAccountBalance());
        setOriginatorConversationID(command.getConversationId());
        setTransactionType(command.getTransactionType());
        setTransactionId(command.getTransactionId());
        setTransactionAmount(command.getTransactionAmount());
        setMsisdn(command.getMsisdn());
        setIsManualPosting(command.getManualPosting());
    }
}
