package com.company.youse.services.query.sysaccount;

import com.company.youse.models.yousepay.OrgShortCode;
import com.company.youse.models.yousepay.SysAccount;
import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.pojo.OrgShortCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class GetOrgShortCodesByMemberIdService extends QueryBaseService<GetShortCodesByMemberIdQuery, Collection<OrgShortCodeDTO>> {

    private final GetSysAccountByMemberIdService getSysAccountByMemberIdService;

    @Autowired
    public GetOrgShortCodesByMemberIdService(GetSysAccountByMemberIdService getSysAccountByMemberIdService) {
        this.getSysAccountByMemberIdService = getSysAccountByMemberIdService;
    }

    @Override
    public QueryResult<Collection<OrgShortCodeDTO>> execute(GetShortCodesByMemberIdQuery query) {

        QueryResult<SysAccount> sysAccountQueryResult = getSysAccountByMemberIdService
                .decorate(new GetSysAccountByMemberIdQuery(query.getMemberId()));

        if (sysAccountQueryResult.isFailure())
            return new QueryResult.Builder<Collection<OrgShortCodeDTO>>().noContent()
                    .message(sysAccountQueryResult.getStrategy().getMessage()).build();

        return new QueryResult.Builder<Collection<OrgShortCodeDTO>>()
                .data(sysAccountQueryResult.getData().getOrgShortCodes().stream().map(OrgShortCode::toDTO)
                        .collect(Collectors.toSet())).ok().build();
    }
}
