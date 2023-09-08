package com.company.youse.services.query.yousepay;

import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.pojo.C2BTransactionDTO;
import com.company.youse.repositrories.yousepay.C2BTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class GetC2BReceiptByTransactionIdService  extends QueryBaseService<GetC2BReceiptByTransactionIdQuery
        , C2BTransactionDTO> {

    private final C2BTransactionRepository c2BTransactionRepository;

    public GetC2BReceiptByTransactionIdService(C2BTransactionRepository c2BTransactionRepository) {
        this.c2BTransactionRepository = c2BTransactionRepository;
    }

    @Override
    public QueryResult<C2BTransactionDTO> execute(GetC2BReceiptByTransactionIdQuery query) {

        var c2BTransaction = c2BTransactionRepository.findByTransactionId(query.getTransactionId());

        return c2BTransaction.map(bTransaction -> new QueryResult
                .Builder<C2BTransactionDTO>()
                .entity(bTransaction)
                .ok()
                .build()).orElseGet(() -> new QueryResult
                .Builder<C2BTransactionDTO>()
                .noContent()
                .build());
    }
}
