package com.company.youse.services.command.yousepay.auth;

import com.company.youse.interfaces.MpesaRequest;
import com.company.youse.platform.services.Command;;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class MpesaClientCommand implements Command {

    private MpesaRequest requestObject;
    private String appKey;
    private String appSecret;
    private String url;
}
