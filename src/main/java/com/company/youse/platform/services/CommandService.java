package com.company.youse.platform.services;

import com.company.youse.platform.result.CommandResult;

public interface CommandService<C, G> {
    default CommandResult<G> execute(C command) {
        return new CommandResult
                .Builder<G>()
                .build();
    }
}
