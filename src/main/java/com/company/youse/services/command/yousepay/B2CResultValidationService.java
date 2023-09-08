package com.company.youse.services.command.yousepay;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Type;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.services.query.yousepay.GetB2CTransactionByTransactionIdQuery;
import com.company.youse.services.query.yousepay.GetB2CTransactionByTransactionIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class B2CResultValidationService extends CommandBaseService<B2CResultValidationCommand, String> {

    private final GetB2CTransactionByTransactionIdService getB2CTransactionByTransactionIdService;

    @Autowired
    public B2CResultValidationService(GetB2CTransactionByTransactionIdService getB2CTransactionByTransactionIdService) {
        this.getB2CTransactionByTransactionIdService = getB2CTransactionByTransactionIdService;
    }


    @Override
    public CommandResult<String> execute(B2CResultValidationCommand command) {

        var getB2CTransactionByIdResult = getB2CTransactionByTransactionIdService.decorate(
                new GetB2CTransactionByTransactionIdQuery(command.getB2CRequestResult().getTransactionID()));

        if (!getB2CTransactionByIdResult.isNoContent()){
            log.error("Duplicate B2CTransaction with transactionId {} found", command.getB2CRequestResult().getTransactionID());

            InternalMessage internalMessage = InternalMessage.builder()
                    .httpStatus(404)
                    .type(Type.FAILURE)
                    .code("400")
                    .message("Duplicate TransactionID found").build();

            return new CommandResult.Builder<String>().message(internalMessage).g(Boolean.FALSE.toString()).build();
        }

        return new CommandResult.Builder<String>().g(Boolean.TRUE.toString()).id(Boolean.TRUE.toString())
                .ok().build();
    }
}
