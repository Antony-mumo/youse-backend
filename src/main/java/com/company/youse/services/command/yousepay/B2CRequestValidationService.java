package com.company.youse.services.command.yousepay;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Type;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.services.query.yousepay.GetB2CRequestByCorrelationIdQuery;
import com.company.youse.services.query.yousepay.GetB2CRequestByCorrelationIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class B2CRequestValidationService extends CommandBaseService<B2CRequestCommand, String> {

    private final GetB2CRequestByCorrelationIdService getB2CRequestByCorrelationIdService;

    public B2CRequestValidationService(GetB2CRequestByCorrelationIdService getB2CRequestByCorrelationIdService) {
        this.getB2CRequestByCorrelationIdService = getB2CRequestByCorrelationIdService;
    }

    @Override
    public CommandResult<String> execute(B2CRequestCommand command) {

        var correlationId = Optional.ofNullable(command.getCorrelationId());

        if (correlationId.isPresent()) {
            var b2CRequestByCorrelationIdResult = getB2CRequestByCorrelationIdService.decorate(
                    new GetB2CRequestByCorrelationIdQuery(correlationId.get()));

            if (!b2CRequestByCorrelationIdResult.isNoContent()){
                log.error("Duplicate Transaction found with conversation Id: {}", correlationId.get());

                var internalMessage = InternalMessage.builder()
                        .httpStatus(400)
                        .code("400")
                        .type(Type.FAILURE)
                        .message("Correlation Id: " + correlationId.get() +" is already present")
                        .build();

                return new CommandResult.Builder<String>()
                        .badRequest(internalMessage)
                        .g(Boolean.FALSE.toString())
                        .id(Boolean.FALSE.toString())
                        .build();
            }
        }

        return new CommandResult.Builder<String>().g(Boolean.TRUE.toString()).id(Boolean.TRUE.toString()).ok().build();

    }
}
