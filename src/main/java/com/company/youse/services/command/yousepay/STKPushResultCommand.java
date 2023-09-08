package com.company.youse.services.command.yousepay;

import com.company.youse.controller.yousepay.body.STKPushResultBody;
import com.company.youse.platform.services.Command;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class STKPushResultCommand implements Command {

    private Map<String, Object > body;

    public STKPushResultCommand(STKPushResultBody stkPushResult) {
        this.body = stkPushResult.getBody();
    }
}
