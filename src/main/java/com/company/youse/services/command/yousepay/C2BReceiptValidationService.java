package com.company.youse.services.command.yousepay;

import com.company.youse.errorHandler.UnknownIPAddressException;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Type;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.utils.AppUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class C2BReceiptValidationService extends CommandBaseService<C2BReceiptCommand, String> {

    @Value("${mpesa.override_pass_internal}")
    private String overridePassInternal;

    @Value("${mpesa.allowed_ips}")
    private String allowedIPs;

    @Override
    public CommandResult<String> execute(C2BReceiptCommand command) {

        try {
            boolean isManualPosting  = validateTransaction(command);
            command.setManualPosting(isManualPosting);
        }catch (Exception e){
            var internalMessage = InternalMessage.builder()
                    .type(Type.FAILURE)
                    .message(e.getLocalizedMessage())
                    .isTechnical(false)
                    .code("400")
                    .httpStatus(400)
                    .build();

            return new CommandResult.Builder<String>().message(internalMessage).build();
        }

        return new CommandResult.Builder<String>().id("SUCCESS").build();
    }

    @SneakyThrows
    private boolean  validateTransaction(C2BReceiptCommand command) {

        String ipAddress = command.getIpAddress();
        if(ipAddress==null) ipAddress = "UNDEFINED_IP";

        if (!AppUtils.StringUtils.isNullOrEmpty(command.getOverrideIPPass())) {
            boolean overrideMatch = overridePassInternal.equals(command.getOverrideIPPass());

            if (overrideMatch) {
                log.warn("Manual Mpesa transaction posting");
                NumberFormat formatter = new DecimalFormat("###,###.00");
                var subject = "BackDated Transaction Posted " + command.getTransactionId() + " of amount "
                        + formatter.format(command.getTransactionAmount());
                var message = " Take note of below transaction posted to " + command.getBillRefNo();
                //TODO should send an email ro *.rolengi.com
                log.warn(" Subject: {}  message {}", subject, message);

                return true;
            }

            List<String> allowedIPsSplit = Arrays.asList(allowedIPs.split(","));

            if (!allowedIPsSplit.contains(allowedIPs)) {
                log.error(">>> Backdated transaction failed to POST!!>> Untrusted IP Address");
                var subject = "Suspect Transaction " + command.getTransactionId() + " from " + ipAddress;
                var message = "Untrusted source sent the following transaction, Kindly investigate this further. <p>";

                //TODO should send an email ro *.rolengi.com
                log.error(" Subject: {}  message {}", subject, message);

                throw new UnknownIPAddressException(ipAddress);
            }
        }

        command.setIpAddress(ipAddress);
        return false;
    }
}
