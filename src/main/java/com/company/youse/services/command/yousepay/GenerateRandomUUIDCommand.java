package com.company.youse.services.command.yousepay;

import com.company.youse.platform.services.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateRandomUUIDCommand implements Command {
    private int leftLimit;
    private int rightLimit;
    private int targetLength;
}
