package com.company.youse.services.command.yousepay;

import com.company.youse.enums.ShortCodeType;
import com.company.youse.models.yousepay.STKPush;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.pojo.STKPushRequest;
import com.company.youse.pojo.STKPushRequestResponseBody;
import com.company.youse.repositrories.yousepay.STKPushRepository;
import com.company.youse.services.command.yousepay.auth.MpesaClientCommand;
import com.company.youse.services.command.yousepay.auth.MpesaClientService;
import com.company.youse.services.query.yousepay.GetOrgCodeByCodeQuery;
import com.company.youse.services.query.yousepay.GetOrgCodeByCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InitiateSTKPushRequestService extends CommandBaseService<STKPushRequestCommand, String> {

    private static final String ACCEPTED = "Accepted for processing";

    @Value("${mpesa.apis.stk_push_callback_url}")
    private String stkPushCallbackUrl;

    @Value("${mpesa.apis.stk_push_callback_url}")
    private String stkPushRequestURL;

    private final MpesaClientService mpesaClientService;
    private final GetOrgCodeByCodeService getOrgCodeByCodeService;

    private final STKPushRepository stkPushRepository;

    public InitiateSTKPushRequestService(MpesaClientService mpesaClientService
            , GetOrgCodeByCodeService getOrgCodeByCodeService, STKPushRepository stkPushRepository) {
        this.mpesaClientService = mpesaClientService;
        this.getOrgCodeByCodeService = getOrgCodeByCodeService;
        this.stkPushRepository = stkPushRepository;
    }

    @SneakyThrows
    @Override
    public CommandResult<String> execute(STKPushRequestCommand command) {
        var stkPush  = new STKPush();

        var stkPushRequest = new STKPushRequest(command);
        stkPushRequest.setCallBackURL(stkPushCallbackUrl);
        stkPush.copyFrom(stkPushRequest);

        var orgShortCodeQueryResult = getOrgCodeByCodeService.decorate(
                new GetOrgCodeByCodeQuery(ShortCodeType.C2B_SHORT_CODE, stkPushRequest.getPartyA()));

        if (orgShortCodeQueryResult.getBase().isFailed())
            return new CommandResult.Builder<String>()
                    .received(orgShortCodeQueryResult.getBase())
                    .build();

        var orShortCode = orgShortCodeQueryResult.getData();

        var mpesaClientCommand = new MpesaClientCommand(stkPushRequest, orShortCode.getApiKey()
                , orShortCode.getApiSecret(), stkPushRequestURL);

        var mpesaClientResult = mpesaClientService.decorate(mpesaClientCommand);

        if (mpesaClientResult.getBase().isFailed()){
            var causes =  mpesaClientResult.getBase();

            var message = causes.getMessages().stream().map(internalMessage ->
                            " [" + internalMessage.getCode() + "] " + internalMessage.getMessage() + " ")
                    .collect(Collectors.joining(" : "));

            stkPush.setResultDescription(message);
            stkPush.setIsAccepted(false);

            stkPushRepository.save(stkPush);

            var internalMessage = InternalMessage.builder()
                    .message(message).
                    build();

            return new CommandResult.Builder<String>().badRequest(internalMessage).build();
        }

        var stkResponse = new STKPushRequestResponseBody();

        //noinspection unchecked
        Map<String, String> response  = new ObjectMapper().readValue(mpesaClientResult.getId(), Map.class) ;
        stkResponse.setAccepted(true);
        stkResponse.setDescription(response.get("ResponseDescription"));
        stkResponse.setMerchantRequestId(response.get("MerchantRequestID"));
        stkResponse.setCheckoutRequestId(response.get("CheckoutRequestID"));
        stkResponse.setCustomerMessage(response.get("CustomerMessage"));
        stkResponse.setDescription(ACCEPTED);

        stkPush.copyFrom(stkResponse);

        var savedRequest = stkPushRepository.save(stkPush);

        return new CommandResult.Builder<String>()
                .id(String.valueOf(savedRequest.getId()))
                .build();
    }
}
