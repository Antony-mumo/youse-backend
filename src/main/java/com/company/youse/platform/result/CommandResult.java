package com.company.youse.platform.result;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.api.rest.InternalServerFailure;
import com.company.youse.platform.api.rest.Ok;
import lombok.Data;

@Data
public class CommandResult<G> {

    private Result base;

    private G id;

    private G data;

    private ResponseStrategy strategy;

    public static class Builder<G> extends ResultBuilder<Builder<G>> {

        private G id;

        public CommandResult<G> build() {

            CommandResult<G> result = new CommandResult<>();

            result.base = new Result(success, retry, strategy, messages);
            result.id = this.id;

            result.strategy = super.strategy(this.strategy, this.success);

            return result;
        }

        @SuppressWarnings("unchecked")
        public Builder<G> id(String id) {
            this.id = (G) id;
            return this;
        }

        public Builder<G> g(G g) {
            this.id = g;
            return this;
        }

        public Builder<G> updated(InternalMessage msg) {
            this.success = true;
            this.strategy = new Ok();
            message(msg);
            return this;
        }

        public Builder<G> notFoundForUpdate(InternalMessage msg) {
            this.success = false;
            this.strategy = new InternalServerFailure();
            message(msg);
            return this;
        }
    }

    public boolean isSuccess() {
        return base.isSuccess();
    }

    public boolean isFailure() {
        return !base.isFailed();
    }
}
