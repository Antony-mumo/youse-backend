package com.company.youse.services.query.sysaccount;

import com.company.youse.controller.yousepay.body.GetSysAccountBody;
import com.company.youse.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetSysAccountByMemberIdQuery implements Query {
    private String memberId;

    public GetSysAccountByMemberIdQuery(GetSysAccountBody body){
        setMemberId(body.getMemberId());
    }
}
