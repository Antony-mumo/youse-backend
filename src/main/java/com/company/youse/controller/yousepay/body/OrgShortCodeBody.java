package com.company.youse.controller.yousepay.body;

import com.company.youse.enums.ShortCodeType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class OrgShortCodeBody {

    private String memberId;

    @Enumerated(EnumType.STRING)
    private ShortCodeType shortCodeType;

    private String shortCode;

    private String c2bReceiptUrl;
    private String stkPushResultUrl;
    private String timeoutUrl;

    private String apiKey;
    private String apiSecret;
}
