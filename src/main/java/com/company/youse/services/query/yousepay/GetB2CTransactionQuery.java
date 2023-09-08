package com.company.youse.services.query.yousepay;

import com.company.youse.platform.services.Query;
import com.company.youse.pojo.TransactionQueryRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetB2CTransactionQuery implements Query {

    private String transactionId;
    private boolean isInvestment;
    private String accountNumber;

    //Required
    private String shortCode;
    private String mpesaAppKey;
    private String mpesaAppSecret;
    private String identifierType;
    private String initiator;
    private String securityCredential;
    private String correlationId;
    private String callbackUrl;
    private String occasion;
    private String remarks;

    private String tenantId;


    public GetB2CTransactionQuery(TransactionQueryRequest request) {
        setTransactionId(request.getTransactionId());
        setInvestment(request.isInvestment());
        setAccountNumber(request.getAccountNumber());

        setShortCode(request.getShortCode());
        setMpesaAppKey(request.getMpesaAppKey());
        setMpesaAppSecret(request.getMpesaAppSecret());
        setIdentifierType(request.getIdentifierType());
        setInitiator(request.getInitiator());
        setSecurityCredential(request.getSecurityCredential());
        setCorrelationId(request.getCorrelationId());
        setCallbackUrl(request.getCallbackUrl());
        setOccasion(request.getOccasion());
        setRemarks(request.getRemarks());

        setTenantId(request.getTenantId());
    }
}
