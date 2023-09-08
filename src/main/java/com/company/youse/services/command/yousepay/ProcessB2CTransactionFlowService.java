package com.company.youse.services.command.yousepay;

import com.company.youse.models.yousepay.B2CTransaction;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.pojo.B2CRequestResult;
import com.company.youse.repositrories.yousepay.B2CTransactionRepository;
import com.company.youse.services.query.yousepay.B2CResultExtractionQuery;
import com.company.youse.services.query.yousepay.B2CResultExtractionService;
import com.company.youse.services.query.yousepay.GetB2CRequestByOriginatorConversationIdQuery;
import com.company.youse.services.query.yousepay.GetB2CRequestByOriginatorConversationIdService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProcessB2CTransactionFlowService extends CommandBaseService<B2CResultCommand, String> {

    private final B2CResultExtractionService b2CResultExtractionService;
    private final B2CResultValidationService b2CResultValidationService;
    private final GetB2CRequestByOriginatorConversationIdService getB2CRequestByOriginatorConversationIdService;

    private final ClientService clientService;
    private final B2CTransactionRepository b2CTransactionRepository;

    public ProcessB2CTransactionFlowService(B2CResultExtractionService b2CResultExtractionService
            , B2CResultValidationService b2CResultValidationService
            , GetB2CRequestByOriginatorConversationIdService getB2CRequestByOriginatorConversationIdService
            , ClientService clientService, B2CTransactionRepository b2CTransactionRepository) {
        this.b2CResultExtractionService = b2CResultExtractionService;
        this.b2CResultValidationService = b2CResultValidationService;
        this.getB2CRequestByOriginatorConversationIdService = getB2CRequestByOriginatorConversationIdService;
        this.clientService = clientService;
        this.b2CTransactionRepository = b2CTransactionRepository;
    }

    @Override
    public CommandResult<String> execute(B2CResultCommand command) {

        B2CRequestResult b2CRequestResult = b2CResultExtractionService.decorate(new B2CResultExtractionQuery(command))
                .getData();

        var validationResult = b2CResultValidationService.decorate(
                new B2CResultValidationCommand(b2CRequestResult));

        if (validationResult.getBase().isFailed()){
            return new CommandResult.Builder<String>()
                    .messages(validationResult.getBase().getMessages())
                    .build();
        }

        var b2cRequest = getB2CRequestByOriginatorConversationIdService
                .decorate(new GetB2CRequestByOriginatorConversationIdQuery
                        (b2CRequestResult.getOriginatorConversationID()))
                .getData();

        var b2CTransaction = new B2CTransaction();
        b2CTransaction.copyFrom(b2CRequestResult);
        b2CTransaction.setB2CRequest(b2cRequest);
        b2CTransaction.setTenantId(b2cRequest.getTenantId());

        b2CRequestResult.setCorrelationID(b2cRequest.getCorrelationId());

        var result = clientService.decorate(new
                ClientCommand(b2CRequestResult, b2cRequest.getCallbackUrl()));

        if (result.getBase().isFailed()){
            b2CTransaction.setCompleted(false);
            var causes =  result.getBase();

            var message = causes.getMessages().stream().map(internalMessage ->
                            " [" + internalMessage.getCode() + "] " + internalMessage.getMessage() + " ")
                    .collect(Collectors.joining(" : "));

            b2CTransaction.setCause(message);
        }else b2CTransaction.setCompleted(true);

        b2CTransaction = b2CTransactionRepository.save(b2CTransaction);

        return new CommandResult.Builder<String>().id(b2CTransaction.getTransactionID()).ok().build();
    }
}
