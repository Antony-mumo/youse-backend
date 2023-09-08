package com.company.youse.services.command.yousepay;

import com.company.youse.enums.AccountEvent;
import com.company.youse.enums.PostingType;
import com.company.youse.models.yousepay.FactAcc;
import com.company.youse.models.yousepay.GlAccount;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.repositrories.yousepay.FactAccRepository;
import com.company.youse.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class OnDealClosedGLFlowExecutorService extends CommandBaseService<OnDealClosedGLFlowExecutorCommand, String> {

    private final FactAccRepository factAccRepository;

    @Autowired
    public OnDealClosedGLFlowExecutorService(FactAccRepository factAccRepository) {
        this.factAccRepository = factAccRepository;
    }

    @Override
    public CommandResult<String> execute(OnDealClosedGLFlowExecutorCommand command) {

        if (command.getAmount().compareTo(BigDecimal.ZERO) < 0)
            return new CommandResult.Builder<String>()
                    .badRequest(InternalMessage.builder()
                            .message("Cannot post -ve values to GL")
                            .build())
                    .build();

        GlAccount merchantDeferredIncomeAccount = command.getMerchantDeferredIncomeAccount();
        GlAccount baseDeferredIncomeAccount = command.getBaseDeferredIncomeAccount();
        GlAccount customerDebtAccount = command.getCustomerDebtAccount();

        var transactionFactRef = IDUtils.generateUUID();

        FactAcc customerFact = new FactAcc();
        customerFact.setAccountNo(customerDebtAccount.getAccountNo());
        customerFact.setAccountName(customerDebtAccount.getAccountName());
        customerFact.setAccountType(customerDebtAccount.getAccountType());
        customerFact.setAccountClass(customerDebtAccount.getAccountClass());
        customerFact.setAmount(command.getAmount());
        customerFact.setCurrency(command.getCurrency());
        customerFact.setDescription("Recognized customer debt yet to be paid after contract ended");
        customerFact.setPostingDate(command.getPostingDate());
        customerFact.setPostingType(PostingType.DR);
        customerFact.setReferencedFactId(transactionFactRef);
        customerFact.setTransactionId(IDUtils.generateId());
        customerFact.setTransactionReference(command.getTransactionReference());
        customerFact.setChannel(command.getTransactionChannel());
        customerFact.setMerchantRefId(command.getCustomerRefId().toString());
        customerFact.setEvent(AccountEvent.ACC_POST);

        FactAcc merchantFact = new FactAcc();
        merchantFact.setAccountNo(merchantDeferredIncomeAccount.getAccountNo());
        merchantFact.setAccountName(merchantDeferredIncomeAccount.getAccountName());
        merchantFact.setAccountType(merchantDeferredIncomeAccount.getAccountType());
        merchantFact.setAccountClass(merchantDeferredIncomeAccount.getAccountClass());
        merchantFact.setAmount(command.getAmount().subtract(command.getInterestToBeCharged()));
        merchantFact.setCurrency(command.getCurrency());
        merchantFact.setDescription("Recognized income not yet earned after contract ended");
        merchantFact.setPostingDate(command.getPostingDate());
        merchantFact.setPostingType(PostingType.DR);
        merchantFact.setReferencedFactId(transactionFactRef);
        merchantFact.setTransactionId(IDUtils.generateId());
        merchantFact.setTransactionReference(command.getTransactionReference());
        merchantFact.setChannel(command.getTransactionChannel());
        merchantFact.setMerchantRefId(command.getMerchantRefId().toString());
        merchantFact.setEvent(AccountEvent.ACC_POST);

        FactAcc deferredYouseIncomeFact = new FactAcc();
        deferredYouseIncomeFact.setAccountNo(baseDeferredIncomeAccount.getAccountNo());
        deferredYouseIncomeFact.setAccountName(baseDeferredIncomeAccount.getAccountName());
        deferredYouseIncomeFact.setAccountType(baseDeferredIncomeAccount.getAccountType());
        deferredYouseIncomeFact.setAccountClass(baseDeferredIncomeAccount.getAccountClass());
        deferredYouseIncomeFact.setAmount(command.getInterestToBeCharged());
        deferredYouseIncomeFact.setCurrency(command.getCurrency());
        deferredYouseIncomeFact.setDescription("Recognized company income not yet earned after contract ended");
        deferredYouseIncomeFact.setPostingDate(command.getPostingDate());
        deferredYouseIncomeFact.setPostingType(PostingType.DR);
        deferredYouseIncomeFact.setReferencedFactId(transactionFactRef);
        deferredYouseIncomeFact.setTransactionId(IDUtils.generateId());
        deferredYouseIncomeFact.setTransactionReference(command.getTransactionReference());
        deferredYouseIncomeFact.setChannel(command.getTransactionChannel());
        deferredYouseIncomeFact.setEvent(AccountEvent.ACC_POST);

        factAccRepository.save(customerFact);
        factAccRepository.save(merchantFact);
        factAccRepository.save(deferredYouseIncomeFact);

        return new CommandResult.Builder<String>()
                .ok().id(transactionFactRef)
                .build();
    }
}
