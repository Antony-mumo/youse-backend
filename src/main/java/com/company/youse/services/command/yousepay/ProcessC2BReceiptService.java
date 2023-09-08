package com.company.youse.services.command.yousepay;

import com.company.youse.enums.ShortCodeType;
import com.company.youse.errorHandler.DuplicateTransactionException;
import com.company.youse.models.yousepay.C2BTransaction;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.repositrories.yousepay.C2BTransactionRepository;
import com.company.youse.services.query.yousepay.GetC2BReceiptByTransactionIdQuery;
import com.company.youse.services.query.yousepay.GetC2BReceiptByTransactionIdService;
import com.company.youse.services.query.yousepay.GetOrgCodeByCodeQuery;
import com.company.youse.services.query.yousepay.GetOrgCodeByCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessC2BReceiptService extends CommandBaseService<C2BReceiptCommand, String> {

    private final C2BTransactionRepository c2BTransactionRepository;
    private final GetC2BReceiptByTransactionIdService getC2BReceiptByTransactionIdService;

    private final GetOrgCodeByCodeService getOrgCodeByCodeService;

    private final C2BReceiptClientPostService c2BReceiptClientPostService;

    public ProcessC2BReceiptService(C2BTransactionRepository c2BTransactionRepository
            , GetC2BReceiptByTransactionIdService getC2BReceiptByTransactionIdService
            , GetOrgCodeByCodeService getOrgCodeByCodeService, C2BReceiptClientPostService c2BReceiptClientPostService) {
        this.c2BTransactionRepository = c2BTransactionRepository;
        this.getC2BReceiptByTransactionIdService = getC2BReceiptByTransactionIdService;
        this.getOrgCodeByCodeService = getOrgCodeByCodeService;
        this.c2BReceiptClientPostService = c2BReceiptClientPostService;
    }

    @Override
    public CommandResult<String> execute(C2BReceiptCommand command) {
        var c2BTransaction = new C2BTransaction();
        c2BTransaction.copy(command);

        var orgShortCodeQueryResult = getOrgCodeByCodeService.decorate(
                new GetOrgCodeByCodeQuery(ShortCodeType.C2B_SHORT_CODE, command.getBusinessShortCode()));

        if (orgShortCodeQueryResult.getBase().isFailed())
            return new CommandResult.Builder<String>()
                    .received(orgShortCodeQueryResult.getBase())
                    .build();

        c2BTransaction.setTenantId(orgShortCodeQueryResult.getData().getSysAccount().getMemberId());
        try {
            validateTransaction(command);
        } catch (Exception e) {
            e.printStackTrace();

            c2BTransaction.setSynced(false);
            c2BTransaction.setSyncMessage(e.getLocalizedMessage());

            InternalMessage internalMessage = InternalMessage.builder()
                    .message(e.getLocalizedMessage())
                    .code("400").build();

            c2BTransactionRepository.save(c2BTransaction);

            return new CommandResult.Builder<String>().badRequest(internalMessage).build();
        }

        c2BTransactionRepository.save(c2BTransaction);

        var postToClientResult = c2BReceiptClientPostService.decorate(
                new C2BReceiptClientPostCommand(c2BTransaction, orgShortCodeQueryResult.getData()));

        if (postToClientResult.getBase().isFailed())
            return new CommandResult.Builder<String>()
                    .received(postToClientResult.getBase())
                    .build();

        return new CommandResult.Builder<String>()
                .id(postToClientResult.getId())
                .build();
    }

    private void validateTransaction(C2BReceiptCommand command)  throws DuplicateTransactionException {

        var getC2BReceiptByTransactionIdQuery = new GetC2BReceiptByTransactionIdQuery();
        getC2BReceiptByTransactionIdQuery.setTransactionId(command.getTransactionId());

        var transactionDTOQueryResult = getC2BReceiptByTransactionIdService
                .decorate(getC2BReceiptByTransactionIdQuery);

        if (transactionDTOQueryResult.isNoContent()){
            log.trace("Transaction with id {} validated", command.getTransactionId());
            return;
        }

        var transactionDTO = transactionDTOQueryResult.getData();

        if (transactionDTO.getMsisdn().equals(command.getMsisdn()) || transactionDTO.getBillRefNo()
                .equals(command.getMsisdn())){

            log.error("Transaction {} rejected! duplicate found ", command);

            throw new DuplicateTransactionException(
                    String.format("Transaction %s rejected! duplicate found ", command)
            );
        }
    }
}
