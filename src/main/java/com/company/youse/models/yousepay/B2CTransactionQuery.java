package com.company.youse.models.yousepay;

import com.company.youse.models.PO;
import com.company.youse.pojo.B2CTransactionQueryRequest;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "b2c_transaction_query")
public class B2CTransactionQuery extends PO  {

    //Request
    private String initiator;
    private String securityCredential;
    private String commandID;
    private String transactionID;
    private String partyA;
    private String identifierType;
    private String resultURL;
    private String queueTimeOutURL;
    private String remarks;
    private String occasion;

    //For Mapping Transaction From Safaricom
    private String originatorConversationId;
    private String conversationID;
    private  String responseCode;
    private String responseDescription;

    private boolean isInvestment;

    private String shortCode;
    private String tenantId;
    private  String correlationId;
    private String callbackUrl;
    private String appKey;
    private String appSecret;

    private boolean accepted;
    private String cause;

    public void copyFrom(B2CTransactionQueryRequest request) {
        setTransactionID(request.getTransactionID());
        setPartyA(request.getPartyA());
        setIdentifierType(request.getIdentifierType());
        setInitiator(request.getInitiator());
        setSecurityCredential(request.getSecurityCredential());
        setResultURL(request.getResultURL());
        setOccasion(request.getOccasion());
        setRemarks(request.getRemarks());
    }
}
