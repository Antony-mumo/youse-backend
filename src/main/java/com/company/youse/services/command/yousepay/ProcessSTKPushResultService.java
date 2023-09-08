package com.company.youse.services.command.yousepay;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.pojo.STKPushResult;
import com.company.youse.repositrories.yousepay.STKPushRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProcessSTKPushResultService extends CommandBaseService<STKPushResultCommand, String> {

    private final STKPushRepository stkPushRepository;

    public ProcessSTKPushResultService(STKPushRepository stkPushRepository) {
        this.stkPushRepository = stkPushRepository;
    }

    @Override
    public CommandResult<String> execute(STKPushResultCommand command) {

        //noinspection unchecked
        Map<String, Object> stkCallBack = (Map<String, Object>) command.getBody().get("stkCallback");
        var checkoutRequestId = stkCallBack.get("CheckoutRequestID");

        var stkPush = stkPushRepository.findByCheckoutRequestId(String.valueOf(checkoutRequestId));

        if (!stkPush.isPresent()){
            log.error("Transaction with req ID {} not found.", checkoutRequestId);

            InternalMessage internalMessage = InternalMessage.builder().message(
                    String.format("Transaction with req ID %s not found.", checkoutRequestId)
            ).httpStatus(404).build();

            return new CommandResult.Builder<String>().notFoundForUpdate(internalMessage).build();
        }

        stkPush.get().setResultDescription(String.valueOf(stkCallBack.get("CheckoutRequestID")));

        int resultCode = (int) stkCallBack.get("ResultCode");

        var stkPushResult = new STKPushResult();
        stkPushResult.setResultCode(resultCode);

        if (resultCode != 0){
            log.error("Transaction with checkout Id {} failed", checkoutRequestId);
        }

        stkPushResult.copyFrom(stkPush.get());
        log.info(stkPushResult.toString());

        stkPush.get().setReceiptNumber(stkPushResult.getReceiptNumber());

        var savedSTKPush = stkPushRepository.save(stkPush.get());




        return new CommandResult.Builder<String>()
                .ok().id(String.valueOf(savedSTKPush.getId()))
                .build();
    }
}
