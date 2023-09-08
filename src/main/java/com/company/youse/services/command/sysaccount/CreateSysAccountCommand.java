package com.company.youse.services.command.sysaccount;

import com.company.youse.controller.yousepay.body.SysAccountBody;
import com.company.youse.platform.services.Command;
import lombok.Data;

@Data
public class CreateSysAccountCommand implements Command {
    private String orgName;
    private String memberId;
    private String contactEmail;
    private String contactPhone;

    public CreateSysAccountCommand(SysAccountBody body){
        setOrgName(body.getOrgName());
        setMemberId(body.getMemberId());
        setContactEmail(body.getContactEmail());
        setContactPhone(body.getContactPhone());
    }
}
