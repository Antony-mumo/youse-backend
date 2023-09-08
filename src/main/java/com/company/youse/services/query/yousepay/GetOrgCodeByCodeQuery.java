package com.company.youse.services.query.yousepay;

import com.company.youse.enums.ShortCodeType;
import com.company.youse.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOrgCodeByCodeQuery implements Query {
    private ShortCodeType shortCodeType;
    private String shortCode;
}
