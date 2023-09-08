package com.company.youse.platform.result;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.api.rest.*;
import com.company.youse.platform.contract.ResponseMessage;
import com.company.youse.platform.contract.Type;
import com.company.youse.utils.AppUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ResultBuilder<B extends ResultBuilder<B>> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected boolean success;

    protected boolean retry;

    protected Set<InternalMessage> messages;

    protected ResponseStrategy strategy;

    public B message(InternalMessage message) {
        if (message == null) {
            return (B) this;
        }
        if (messages == null) {
            messages = new HashSet<>();
        }
        if (!messages.contains(message)) {
            messages.add(message);
        }
        return (B) this;
    }

    public B message(ResponseMessage message) {
        if (message == null) {
            return (B) this;
        }
        message(message.to());
        return (B) this;
    }

    public B messages(Set<InternalMessage> list) {
        if (AppUtils.Objects.isEmpty(list)) {
            return (B) this;
        }
        list.forEach(this::message);
        return (B) this;
    }

    public B noContent() {
        this.success = true;
        this.strategy = new NoContent();
        this.message(this.strategy.getMessage());
        return (B) this;
    }

    public B ok() {
        if (!hasAnyNonSuccessMessage()) {
            this.success = true;
            this.strategy = new Ok();
            message(this.strategy.getMessage());
        }
        return (B) this;
    }

    public B received(Result base) {
        this.success = base.success;
        this.retry = base.retry;
        this.messages = base.messages;
        this.strategy = base.strategy;
        return (B) this;
    }

    private boolean hasAnyNonSuccessMessage() {
        return AppUtils.Objects.isPresent(messages) && messages.stream().anyMatch(t -> !Type.SUCCESS.equals(t.getType()));
    }

    public B clientError(HttpStatus httpStatus, String message) {
        this.success = false;
        this.strategy = new ClientError(httpStatus, message);
        message(this.strategy.getMessage());
        return (B) this;
    }

    public B notFound() {
        this.success = false;
        this.strategy = new NotFound();
        message(this.strategy.getMessage());
        return (B) this;
    }

    public B badRequest(Set<InternalMessage> messages) {
        this.success = false;
        this.strategy = new BadRequest();
        messages(messages);
        return (B) this;
    }

    public B badRequest(InternalMessage message) {
        Set<InternalMessage> set = new HashSet<>();
        set.add(message);
        return badRequest(set);
    }

    public B internalServerFailure() {
        this.success = false;
        this.strategy = new InternalServerFailure();
        if (AppUtils.Objects.isEmpty(this.messages)) {
            message(this.strategy.getMessage());
        }
        return (B) this;
    }



    public ResponseStrategy strategy(ResponseStrategy strategy, boolean success) {
        if (strategy == null) {
            this.strategy = success ? new Ok() : new InternalServerFailure();
            LOGGER.debug("Strategy assumed as {}", this.strategy.getClass().getSimpleName());
        } else {
            this.strategy = strategy;
        }
        return this.strategy;
    }

}

