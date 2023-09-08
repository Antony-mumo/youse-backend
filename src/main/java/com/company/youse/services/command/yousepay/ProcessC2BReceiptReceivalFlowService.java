package com.company.youse.services.command.yousepay;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Type;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import org.springframework.stereotype.Service;

@Service
public class ProcessC2BReceiptReceivalFlowService extends CommandBaseService<C2BReceiptCommand, String> {

    private final C2BReceiptValidationService c2BReceiptValidationService;
    private final ProcessC2BReceiptService processC2BReceiptService;

    public ProcessC2BReceiptReceivalFlowService(C2BReceiptValidationService c2BReceiptValidationService
            , ProcessC2BReceiptService processC2BReceiptService) {
        this.c2BReceiptValidationService = c2BReceiptValidationService;
        this.processC2BReceiptService = processC2BReceiptService;
    }

    @Override
    public CommandResult<String> execute(C2BReceiptCommand command) {

        var validationResult =  c2BReceiptValidationService.decorate(command);

        if (validationResult.getBase().isFailed()){
            return new CommandResult.Builder<String>().message(InternalMessage.builder()
                    .type(Type.FAILURE)
                    .message("validation message failed")
                    .isTechnical(false)
                    .code("400")
                    .httpStatus(400)
                    .build()).messages(validationResult.getBase().getMessages()).build();
        }

        var c2bReceiptResult = processC2BReceiptService.decorate(command);

        if (validationResult.getBase().isFailed())
            return new CommandResult.Builder<String>()
                    .received(c2bReceiptResult.getBase())
                    .build();

        return new CommandResult.Builder<String>().ok().id(c2bReceiptResult.getId()).build();
    }
}
