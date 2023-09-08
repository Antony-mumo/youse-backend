package com.company.youse.services.command.yousepay;

import com.company.youse.controller.yousepay.body.B2CResultBody;
import com.company.youse.platform.services.Command;
import lombok.Data;

import java.util.Map;

@Data
public class B2CResultCommand  implements Command {

    private Map<String, Object> result;

    public B2CResultCommand(B2CResultBody b2CResultBody) {
        this.result = b2CResultBody.getResult();
    }
}