package com.company.youse.platform.decorator.command;

import com.company.youse.platform.result.CommandResult;
import com.company.youse.platform.services.Command;

import javax.annotation.PostConstruct;

public abstract class CommandBaseService<C extends Command, G> extends CommandDecorator<C, G> {

    public CommandBaseService() {
        super(null);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void postConstruct() {
        decorated = decors();
        decorated.init(this);
    }

    @Override
    public CommandResult<G> decorate(C command) {
        return decorated.decorate(command);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static CommandDecorator decors() {

        //TODO Yet to write command finishing for wrapping up commands
        return new CommandBus(null);
    }

}
