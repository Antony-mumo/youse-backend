package com.company.youse.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionQueryRequest {

    @JsonProperty("transactionId")
    private String transactionId;
    private boolean isInvestment;
    private String accountNumber;

    //Required
    @JsonProperty("shortCode")
    private String shortCode;
    @JsonProperty("mpesaAppKey")
    private String mpesaAppKey;
    @JsonProperty("mpesaAppSecret")
    private String mpesaAppSecret;
    @JsonProperty("partyA")
    private String partyA;
    @JsonProperty("identifierType")
    private String identifierType;
    @JsonProperty("initiatorName")
    private String initiator;
    @JsonProperty("securityCredential")
    private String securityCredential;
    @JsonProperty("correlationId")
    private String correlationId;
    @JsonProperty("callbackUrl")
    private String callbackUrl;
    @JsonProperty("occasion")
    private String occasion;
    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("tenantId")
    private String tenantId;
}
