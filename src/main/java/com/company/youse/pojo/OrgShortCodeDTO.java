package com.company.youse.pojo;

import com.company.youse.enums.ShortCodeType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrgShortCodeDTO {
    private Long id;

    private ShortCodeType shortCodeType;

    private String shortCode;

    private BigDecimal totalTransactedAmount;

    private String c2bReceiptUrl;
    private String stkPushResultUrl;
    private String timeoutUrl;

    private String apiKey;
    private String apiSecret;
}