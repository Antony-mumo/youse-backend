package com.company.youse.services.query.sysaccount;

import com.company.youse.controller.yousepay.body.GetOrgShortCodesBody;
import com.company.youse.platform.services.Query;
import lombok.Data;

@Data
public class GetShortCodesByMemberIdQuery implements Query {
    private String memberId;
    public GetShortCodesByMemberIdQuery(GetOrgShortCodesBody getOrgShortCodesBody) {
        setMemberId(getOrgShortCodesBody.getMemberId());
    }
}
