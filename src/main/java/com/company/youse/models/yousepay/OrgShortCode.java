package com.company.youse.models.yousepay;

import com.company.youse.enums.ShortCodeType;
import com.company.youse.models.PO;
import com.company.youse.platform.data.Transferable;
import com.company.youse.pojo.OrgShortCodeDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "u_org_short_codes")
public class OrgShortCode extends PO implements Transferable<OrgShortCodeDTO> {

    public static final String CLASSNAME = "OrgShortCode";

    @Enumerated(EnumType.STRING)
    private ShortCodeType shortCodeType;

    private String shortCode;

    private BigDecimal totalTransactedAmount;

    private String c2bReceiptUrl;
    private String timeoutUrl;
    private String stkPushResultUrl;

    private String apiKey;
    private String apiSecret;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sys_account_id")
    private SysAccount sysAccount;

    @Override
    public OrgShortCodeDTO toDTO() {
        var orgShortCode = new OrgShortCodeDTO();
        orgShortCode.setShortCode(getShortCode());
        orgShortCode.setShortCodeType(getShortCodeType());
        orgShortCode.setApiKey(getApiKey());
        orgShortCode.setApiSecret(getApiSecret());
        orgShortCode.setC2bReceiptUrl(getC2bReceiptUrl());
        orgShortCode.setStkPushResultUrl(getStkPushResultUrl());
        orgShortCode.setTimeoutUrl(getTimeoutUrl());
        return orgShortCode;
    }
}
