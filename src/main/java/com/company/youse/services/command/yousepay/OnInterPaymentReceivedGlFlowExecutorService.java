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
public class OnInterPaymentReceivedGlFlowExecutorService extends
        CommandBaseService<OnInterPaymentReceivedGlFlowExecutorCommand, String> {

    private final FactAccRepository factAccRepository;

    @Autowired
    public OnInterPaymentReceivedGlFlowExecutorService(FactAccRepository factAccRepository) {
        this.factAccRepository = factAccRepository;
    }

    @Override
    public CommandResult<String> execute(OnInterPaymentReceivedGlFlowExecutorCommand command) {

        if (command.getAmount().compareTo(BigDecimal.ZERO) < 0)
            return new CommandResult.Builder<String>()
                    .badRequest(InternalMessage.builder()
                            .message("Cannot post -ve values to GL")
                            .build())
                    .build();

        GlAccount customerDebtAccount = command.getCustomerDebtAccount();
        GlAccount merchantDeferredIncomeAccount = command.getMerchantDeferredIncomeAccount();
        GlAccount baseDeferredIncomeAccount = command.getBaseDeferredIncomeAccount();
        GlAccount baseIncomeAccount = command.getBaseIncomeAccount();
        GlAccount baseCashAccount = command.getBaseCashAccount();

        var transactionFactRef = IDUtils.generateUUID();

        FactAcc customerFact = new FactAcc();
        customerFact.setAccountNo(customerDebtAccount.getAccountNo());
        customerFact.setAccountName(customerDebtAccount.getAccountName());
        customerFact.setAccountType(customerDebtAccount.getAccountType());
        customerFact.setAccountClass(customerDebtAccount.getAccountClass());
        customerFact.setCurrency(command.getCurrency());
        customerFact.setAmount(command.getAmount());
        customerFact.setDescription("Recognized customer debt paid after contract ended");
        customerFact.setPostingDate(command.getPostingDate());
        customerFact.setPostingType(PostingType.CR);
        customerFact.setReferencedFactId(transactionFactRef);
        customerFact.setTransactionId(IDUtils.generateId());
        customerFact.setTransactionReference(command.getTransactionReference());
        customerFact.setChannel(command.getTransactionChannel());
        customerFact.setMerchantRefId(command.getCustomerRefId().toString());
        customerFact.setEvent(AccountEvent.ACC_POST);

        FactAcc merchantFact = new FactAcc();
        merchantFact.setAccountNo(merchantDeferredIncomeAccount.getAccountNo());
        merchantFact.setAccountName(merchantDeferredIncomeAccount.getAccountName());
        merchantFact.setAccountClass(merchantDeferredIncomeAccount.getAccountClass());
        merchantFact.setAccountType(merchantDeferredIncomeAccount.getAccountType());
        merchantFact.setAmount(command.getAmount().subtract(command.getInterestToBeCharged()));
        merchantFact.setCurrency(command.getCurrency());
        merchantFact.setDescription("Recognized income earned after contract ended");
        merchantFact.setPostingDate(command.getPostingDate());
        merchantFact.setPostingType(PostingType.CR);
        merchantFact.setReferencedFactId(transactionFactRef);
        merchantFact.setTransactionId(IDUtils.generateId());
        merchantFact.setTransactionReference(command.getTransactionReference());
        merchantFact.setChannel(command.getTransactionChannel());
        merchantFact.setMerchantRefId(command.getMerchantRefId().toString());
        merchantFact.setEvent(AccountEvent.ACC_POST);

        FactAcc deferredYouseIncomeFact = new FactAcc();
        deferredYouseIncomeFact.setAccountNo(baseDeferredIncomeAccount.getAccountNo());
        deferredYouseIncomeFact.setAccountName(baseDeferredIncomeAccount.getAccountName());
        deferredYouseIncomeFact.setAccountClass(baseDeferredIncomeAccount.getAccountClass());
        deferredYouseIncomeFact.setAccountType(baseDeferredIncomeAccount.getAccountType());
        deferredYouseIncomeFact.setAmount(command.getInterestToBeCharged());
        deferredYouseIncomeFact.setCurrency(command.getCurrency());
        deferredYouseIncomeFact.setDescription("Recognized company income earned after contract ended");
        deferredYouseIncomeFact.setPostingDate(command.getPostingDate());
        deferredYouseIncomeFact.setPostingType(PostingType.CR);
        deferredYouseIncomeFact.setReferencedFactId(transactionFactRef);
        deferredYouseIncomeFact.setTransactionId(IDUtils.generateId());
        deferredYouseIncomeFact.setTransactionReference(command.getTransactionReference());
        deferredYouseIncomeFact.setChannel(command.getTransactionChannel());
        deferredYouseIncomeFact.setEvent(AccountEvent.ACC_POST);

        FactAcc baseIncomeFact = new FactAcc();
        baseIncomeFact.setAccountName(baseIncomeAccount.getAccountName());
        baseIncomeFact.setAccountNo(baseIncomeAccount.getAccountNo());
        baseIncomeFact.setAccountType(baseIncomeAccount.getAccountType());
        baseIncomeFact.setAccountClass(baseIncomeAccount.getAccountClass());
        baseIncomeFact.setAmount(command.getInterestToBeCharged());
        baseIncomeFact.setCurrency(command.getCurrency());
        baseIncomeFact.setDescription("Recognized company income earned after contract payment");
        baseIncomeFact.setPostingDate(command.getPostingDate());
        baseIncomeFact.setPostingType(PostingType.DR);
        baseIncomeFact.setReferencedFactId(transactionFactRef);
        baseIncomeFact.setTransactionReference(command.getTransactionReference());
        baseIncomeFact.setTransactionId(IDUtils.generateId());
        baseIncomeFact.setChannel(command.getTransactionChannel());
        baseIncomeFact.setEvent(AccountEvent.ACC_POST);

        FactAcc baseCashFact = new FactAcc();
        baseCashFact.setAccountName(baseCashAccount.getAccountName());
        baseCashFact.setAccountType(baseCashAccount.getAccountType());
        baseCashFact.setAccountClass(baseCashAccount.getAccountClass());
        baseCashFact.setAmount(command.getInterestToBeCharged());
        baseCashFact.setAccountNo(baseCashAccount.getAccountNo());
        baseCashFact.setCurrency(command.getCurrency());
        baseCashFact.setDescription("Recognized company cash deposit after contract payment");
        baseCashFact.setPostingDate(command.getPostingDate());
        baseCashFact.setPostingType(PostingType.DR);
        baseCashFact.setTransactionId(IDUtils.generateId());
        baseCashFact.setReferencedFactId(transactionFactRef);
        baseCashFact.setTransactionReference(command.getTransactionReference());
        baseCashFact.setChannel(command.getTransactionChannel());
        baseCashFact.setEvent(AccountEvent.ACC_POST);

        factAccRepository.save(customerFact);
        factAccRepository.save(merchantFact);
        factAccRepository.save(deferredYouseIncomeFact);
        factAccRepository.save(baseIncomeFact);
        factAccRepository.save(baseCashFact);

        return new CommandResult.Builder<String>()
                .ok().id(transactionFactRef)
                .build();
    }
}
