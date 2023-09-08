package com.company.youse.services.command.yousepay;

import com.company.youse.enums.Currency;
import com.company.youse.enums.TransactionChannel;
import com.company.youse.models.yousepay.GlAccount;
import com.company.youse.platform.services.Command;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OnDealClosedGLFlowExecutorCommand implements Command {
    private GlAccount merchantDeferredIncomeAccount;
    private Long merchantRefId;

    private GlAccount baseDeferredIncomeAccount;
    private GlAccount customerDebtAccount;
    private Long customerRefId;

    private BigDecimal amount;
    private BigDecimal interestToBeCharged;
    private String transactionReference;
    private Date postingDate;

    private TransactionChannel transactionChannel;
    private Currency currency;
}
