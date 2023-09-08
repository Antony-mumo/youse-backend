package com.company.youse.services.command.yousepay;

import com.company.youse.interfaces.ClientResponse;
import com.company.youse.platform.services.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientCommand implements Command {

    private ClientResponse clientResponse;
    private String url;
}

