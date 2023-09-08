package com.company.youse.services.command.yousepay;

import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import org.springframework.stereotype.Service;

@Service
public class RegisterC2BUrlService extends CommandBaseService<RegisterUrlCommand,String> {

    @Override
    public CommandResult<String> execute(RegisterUrlCommand command) {
        return super.execute(command);
    }
}
