package com.company.youse.services.query.yousepay;


import com.company.youse.platform.services.Query;
import lombok.Data;

@Data
public class GetC2BReceiptByTransactionIdQuery implements Query {
    private String transactionId;

    @Override
    public String identifier() {
        return transactionId;
    }
}
