package com.company.youse.services.command.yousepay.auth;

import com.company.youse.platform.services.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MpesaAuthenticationCommand implements Command {
    private String appKey;
    private String appSecret;
}
