package com.company.youse.platform.decorator.query;


import com.company.youse.platform.log.DecorLogger;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.platform.services.Query;
import com.company.youse.platform.services.QueryService;

public abstract class QueryDecorator<Q extends Query, R> implements QueryService<Q, R> {

    protected QueryDecorator<Q, R> decorated;

    protected QueryService<Q, R> last;

    public QueryDecorator(QueryDecorator<Q, R> decorated) {
        this.decorated = decorated;
    }

    public QueryResult<R> decorate(Q query) {

        String decor = this.getClass().getSimpleName();
        String name = query.getClass().getSimpleName();

        QueryResult<R> result;

        if (decorated == null) {
            DecorLogger.logServiceStart(name);
            result = last.execute(query);
            DecorLogger.logServiceFinish(name);
        } else {
            DecorLogger.logDecorStart(name, decor);
            result = decorated.decorate(query);
            DecorLogger.logDecorFinish(name, decor);
        }

        return result;
    }

    public void init(QueryDecorator<Q, R> decorator) {
        if (this.decorated == null) {
            this.last = decorator;
        } else {
            this.decorated.init(decorator);
        }
    }

}
