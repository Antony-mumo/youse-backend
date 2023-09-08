package com.company.youse.services.command.yousepay;

import com.company.youse.platform.services.Command;
import com.company.youse.pojo.B2CRequestResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class B2CResultValidationCommand implements Command {
    private B2CRequestResult b2CRequestResult;
}