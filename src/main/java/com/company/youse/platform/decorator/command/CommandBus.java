package com.company.youse.platform.decorator.command;

import com.company.youse.platform.result.CommandResult;
import com.company.youse.platform.log.DecorLogger;
import com.company.youse.platform.services.Command;

public class CommandBus<C extends Command, G>  extends CommandDecorator<C, G> {

    public CommandBus(CommandDecorator<C, G> decorated) {
        super(decorated);
    }


    @Override
    public CommandResult<G> decorate(C command) {

        String name = command.getClass().getSimpleName();

        DecorLogger.logBusStart(name);

        CommandResult<G> result = super.decorate(command);

        DecorLogger.logBusFinish(name);

        return result;

    }

}
