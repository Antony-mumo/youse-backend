package com.company.youse.platform.decorator.command;

import com.company.youse.platform.result.CommandResult;
import com.company.youse.platform.log.DecorLogger;
import com.company.youse.platform.services.Command;
import com.company.youse.platform.services.CommandService;

public abstract class CommandDecorator<C extends Command, G> implements CommandService<C, G> {

    protected CommandDecorator<C, G> decorated;

    protected CommandService<C, G> last;

    public CommandDecorator(CommandDecorator<C, G> decorated) {
        this.decorated = decorated;
    }

    public CommandResult<G> decorate(C command) {

        String decor = this.getClass().getSimpleName();
        String name = command.getClass().getSimpleName();

        CommandResult<G> result;

        if (decorated == null) {
            DecorLogger.logServiceStart(name);
            result = last.execute(command);
            DecorLogger.logServiceFinish(name);
        } else {
            DecorLogger.logDecorStart(name, decor);
            result = decorated.decorate(command);
            DecorLogger.logDecorFinish(name, decor);
        }
        return result;
    }

    public void init(CommandDecorator<C, G> decorator) {
        if (this.decorated == null) {
            this.last = decorator;
        } else {
            this.decorated.init(decorator);
        }
    }

}

