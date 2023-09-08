package com.company.youse.services.command.sysaccount;

import com.company.youse.controller.yousepay.body.OrgShortCodeBody;
import com.company.youse.enums.ShortCodeType;
import com.company.youse.platform.services.Command;
import lombok.Data;

@Data
public class CreateOrgShortCodeCommand implements Command {

    private String memberId;

    private ShortCodeType shortCodeType;

    private String shortCode;

    private String c2bReceiptUrl;
    private String stkPushResultUrl;
    private String timeoutUrl;

    private String apiKey;
    private String apiSecret;

    public CreateOrgShortCodeCommand(OrgShortCodeBody body) {
        setMemberId(body.getMemberId());
        setShortCodeType(body.getShortCodeType());
        setShortCode(body.getShortCode());
        setC2bReceiptUrl(body.getC2bReceiptUrl());
        setStkPushResultUrl(body.getStkPushResultUrl());
        setTimeoutUrl(body.getTimeoutUrl());
        setApiKey(body.getApiKey());
        setApiSecret(body.getApiSecret());
    }
}
