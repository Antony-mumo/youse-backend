package com.company.youse.platform.decorator.query;


import com.company.youse.platform.result.QueryResult;
import com.company.youse.platform.services.Query;

import javax.annotation.PostConstruct;

public abstract class QueryBaseService<Q extends Query, R> extends QueryDecorator<Q, R> {

    public QueryBaseService() {
        super(null);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct()
    public void postConstruct() {
        decorated = decors();
        decorated.init(this);
    }

    @Override
    public QueryResult<R> decorate(Q query) {
        return decorated.decorate(query);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static QueryDecorator decors() {
        return new QueryBus(null);
    }

}
