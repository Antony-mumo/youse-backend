package com.company.youse.platform.result;


import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.api.rest.NoContent;
import com.company.youse.platform.data.Transferable;
import lombok.Data;

import java.util.Optional;

@Data
public class QueryResult<R> {

    private Result base;

    private R data;

    private ResponseStrategy strategy;

    public static final class Builder<R> extends ResultBuilder<Builder<R>> {

        private R data;

        public QueryResult<R> build() {

            QueryResult<R> result = new QueryResult<>();
            result.base = new Result(success, retry, strategy, messages);
            result.data = this.data;
            result.strategy = super.strategy(this.strategy, this.success);
            return result;
        }

        public Builder<R> data(R data) {
            if (data == null) {
                return notFound();
            }
            this.data = data;
            return this;
        }

        public Builder<R> transferable(Transferable<R> transferable) {
            if (transferable != null) {
                data(transferable.toDTO());
            }
            return this;
        }

        public <D extends Transferable<R>> Builder<R> optionalEntity(Optional<D> optional) {
            if (optional.isPresent()) {
                return data(optional.get().toDTO()).ok();
            } else {
                return notFound();
            }
        }

        public <D extends Transferable<R>> Builder<R> entity(D d) {
            if (d != null) {
                return data(d.toDTO()).ok();
            } else {
                return notFound();
            }
        }

    }

    public boolean isNoContent() {
        return base.strategy instanceof NoContent;
    }

    public boolean isSuccess() {
        return base.success;
    }

    public boolean isFailure() {
        return !base.success;
    }
}
