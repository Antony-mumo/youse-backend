package com.company.youse.services.query.yousepay;

import com.company.youse.models.yousepay.B2CTransaction;
import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.repositrories.yousepay.B2CTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetB2CTransactionByTransactionIdService extends QueryBaseService<GetB2CTransactionByTransactionIdQuery
        , B2CTransaction> {

    private final B2CTransactionRepository b2CTransactionRepository;

    @Autowired
    public GetB2CTransactionByTransactionIdService(B2CTransactionRepository b2CTransactionRepository) {
        this.b2CTransactionRepository = b2CTransactionRepository;
    }

    @Override
    public QueryResult<B2CTransaction> execute(GetB2CTransactionByTransactionIdQuery query) {

        Optional<B2CTransaction> b2CTransaction = b2CTransactionRepository.findByTransactionID(query.getTransactionId());

        return b2CTransaction.map(cTransaction -> new QueryResult.Builder<B2CTransaction>()
                .data(cTransaction).build()).orElseGet(() -> new QueryResult.Builder<B2CTransaction>()
                .noContent().build());

    }
}
