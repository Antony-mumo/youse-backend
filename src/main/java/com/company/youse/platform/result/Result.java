package com.company.youse.platform.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.api.ResponseStrategy;
import com.company.youse.platform.constants.Messages;
import com.company.youse.platform.contract.ResponseMessage;
import com.company.youse.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@ToString
public class Result {

    protected boolean success;

    @JsonIgnore
    protected boolean retry;

    @JsonIgnore
    protected ResponseStrategy strategy;

    protected Set<InternalMessage> messages;

    public List<ResponseMessage> toMessages() {

        List<ResponseMessage> result = new ArrayList<>();

        if (AppUtils.Objects.isEmpty(this.messages)) {
            if (isSuccess()) {
                result.add(Messages.SUCCESS.toMessage());
            } else {
                result.add(Messages.FAILED.toMessage());
            }
        } else {
            this.messages.forEach(m -> result.add(m.toMessage()));
        }

        return result;
    }

    @JsonIgnore
    public boolean isFailed() {
        return !success;
    }

}
