package com.company.youse.services.query.yousepay;

import com.company.youse.models.yousepay.B2CRequest;
import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.repositrories.yousepay.B2CRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class GetB2CRequestByOriginatorConversationIdService
        extends QueryBaseService<GetB2CRequestByOriginatorConversationIdQuery, B2CRequest> {

    private final B2CRequestRepository b2CRequestRepository;

    public GetB2CRequestByOriginatorConversationIdService(B2CRequestRepository b2CRequestRepository) {
        this.b2CRequestRepository = b2CRequestRepository;
    }

    @Override
    public QueryResult<B2CRequest> execute(GetB2CRequestByOriginatorConversationIdQuery query) {

        var b2cRequest = b2CRequestRepository.findByOriginatorConversationId(query.getConversationId());

        return b2cRequest.map(b2CRequest -> new QueryResult.Builder<B2CRequest>()
                .data(b2CRequest)
                .build()).orElseGet(() -> new QueryResult.Builder<B2CRequest>()
                .noContent()
                .build());

    }
}
