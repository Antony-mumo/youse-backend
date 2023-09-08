package com.company.youse.services.command.yousepay;

import com.company.youse.models.yousepay.C2BTransaction;
import com.company.youse.models.yousepay.OrgShortCode;
import com.company.youse.platform.services.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class C2BReceiptClientPostCommand implements Command {
    private C2BTransaction c2BTransaction;
    private OrgShortCode orgShortCode;
}
