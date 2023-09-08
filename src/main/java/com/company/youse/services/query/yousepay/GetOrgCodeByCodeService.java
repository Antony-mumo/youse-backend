package com.company.youse.services.query.yousepay;


import com.company.youse.models.yousepay.OrgShortCode;
import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.repositrories.yousepay.OrgShortCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrgCodeByCodeService extends QueryBaseService<GetOrgCodeByCodeQuery, OrgShortCode> {

    private final OrgShortCodeRepository orgShortCodeRepository;

    @Autowired
    public GetOrgCodeByCodeService(OrgShortCodeRepository orgShortCodeRepository) {
        this.orgShortCodeRepository = orgShortCodeRepository;
    }

    @Override
    public QueryResult<OrgShortCode> execute(GetOrgCodeByCodeQuery query) {

        var orgShortCode = orgShortCodeRepository.findByShortCodeTypeAndShortCode(
                query.getShortCodeType(), query.getShortCode());

        return orgShortCode.map(shortCode -> new QueryResult
                .Builder<OrgShortCode>()
                .data(shortCode)
                .ok()
                .build()).orElseGet(() -> new QueryResult
                .Builder<OrgShortCode>()
                .notFound()
                .build());

    }
}

