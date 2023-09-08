package com.company.youse.services.command.yousepay;


import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GenerateRandomUUIDService extends CommandBaseService<GenerateRandomUUIDCommand, String> {

    @Override
    public CommandResult<String> execute(GenerateRandomUUIDCommand command) {

        Random random = ThreadLocalRandom.current();

        String generatedString = random.ints(command.getLeftLimit(), command.getRightLimit() + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(command.getTargetLength())
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return new CommandResult.Builder<String>().id(generatedString).ok().build();
    }
}

