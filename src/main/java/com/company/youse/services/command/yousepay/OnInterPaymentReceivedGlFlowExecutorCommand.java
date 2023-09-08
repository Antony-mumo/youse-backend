package com.company.youse.services.command.yousepay;

import com.company.youse.enums.Currency;
import com.company.youse.enums.TransactionChannel;
import com.company.youse.models.yousepay.GlAccount;
import com.company.youse.platform.services.Command;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OnInterPaymentReceivedGlFlowExecutorCommand implements Command {
    private GlAccount customerDebtAccount;
    private Long customerRefId;

    private GlAccount merchantDeferredIncomeAccount;
    private GlAccount merchantCashAccount;
    private Long merchantRefId;

    private GlAccount baseDeferredIncomeAccount;
    private GlAccount baseIncomeAccount;
    private GlAccount baseCashAccount;

    private BigDecimal amount;
    private BigDecimal interestToBeCharged;
    private String transactionReference;
    private Date postingDate;

    private TransactionChannel transactionChannel;
    private Currency currency;
}
