package com.company.youse.platform.services;

import com.company.youse.platform.result.QueryResult;

public interface QueryService<Q extends Query, R> {

    default QueryResult<R> execute(Q query) {
        return new QueryResult
                .Builder<R>()
                .build();
    }
}