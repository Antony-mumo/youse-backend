package com.company.youse.platform.decorator.query;


import com.company.youse.platform.log.DecorLogger;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.platform.services.Query;

public class QueryBus<Q extends Query, R> extends QueryDecorator<Q, R> {

    public QueryBus(QueryDecorator<Q, R> decorated) {
        super(decorated);
    }

    @Override
    public QueryResult<R> decorate(Q query) {

        String name = query.getClass().getSimpleName();

        DecorLogger.logBusStart(name);

        QueryResult<R> result = super.decorate(query);

        DecorLogger.logBusFinish(name);

        return result;
    }
}
