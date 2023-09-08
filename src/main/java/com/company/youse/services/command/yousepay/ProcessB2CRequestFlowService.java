package com.company.youse.services.command.yousepay;

import com.company.youse.models.yousepay.B2CRequest;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.pojo.B2CRequestPayload;
import com.company.youse.repositrories.yousepay.B2CRequestRepository;
import com.company.youse.services.command.yousepay.auth.MpesaClientCommand;
import com.company.youse.services.command.yousepay.auth.MpesaClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessB2CRequestFlowService extends CommandBaseService<B2CRequestCommand, String> {

    @Value("${mpesa.b2c_result_url}")
    private String resultURL;

    @Value("${mpesa.b2c_request_url}")
    private String b2cRequestURL;

    @Value("${mpesa.b2b_request_url}")
    private String b2bRequestURL;

    private final B2CRequestValidationService b2CRequestValidationService;
    private final GenerateRandomUUIDService generateRandomUUIDService;

    private final MpesaClientService mpesaClientService;

    private final B2CRequestRepository b2CRequestRepository;

    public ProcessB2CRequestFlowService(B2CRequestValidationService b2CRequestValidationService
            , GenerateRandomUUIDService generateRandomUUIDService, MpesaClientService mpesaClientService
            , B2CRequestRepository b2CRequestRepository) {
        this.b2CRequestValidationService = b2CRequestValidationService;
        this.generateRandomUUIDService = generateRandomUUIDService;
        this.mpesaClientService = mpesaClientService;
        this.b2CRequestRepository = b2CRequestRepository;
    }

    @SneakyThrows
    @Override
    public CommandResult<String> execute(B2CRequestCommand command) {

        var validationResult = b2CRequestValidationService.decorate(command);

        if (!validationResult.getBase().isSuccess()){
            return new CommandResult.Builder<String>()
                    .badRequest(validationResult.getBase().getMessages())
                    .build();
        }

        if (!Optional.ofNullable(command.getCorrelationId()).isPresent()){
            var correlationID = generateRandomUUIDService.decorate(new
                    GenerateRandomUUIDCommand(48, 122, 15)).getId();
            command.setCorrelationId(correlationID);
        }

        var b2cRequest = new B2CRequest(command);
        b2cRequest.setMpesaAppKey(command.getMpesaAppKey().substring(0, 5).concat("*****"));
        b2cRequest.setMpesaAppSecret(command.getMpesaAppSecret().substring(0, 5).concat("******"));

        var b2CRequestPayload = new B2CRequestPayload(command);
        b2CRequestPayload.setResultURL(resultURL);

        var url = command.getIsB2B() ? b2bRequestURL : b2cRequestURL;

        var mpesaClientCommand = new MpesaClientCommand(b2CRequestPayload, command.getMpesaAppKey()
                , command.getMpesaAppSecret(), url);

        var mpesaClientResult = mpesaClientService.decorate(mpesaClientCommand);

        if (mpesaClientResult.getBase().isFailed()){
            var causes =  mpesaClientResult.getBase();

            var message = causes.getMessages().stream().map(internalMessage ->
                            " [" + internalMessage.getCode() + "] " + internalMessage.getMessage() + " ")
                    .collect(Collectors.joining(" : "));

            b2cRequest.setErrorCause(message);
            b2cRequest.setIsAccepted(false);

           b2CRequestRepository.save(b2cRequest);

            var internalMessage = InternalMessage.builder()
                    .message(message).
                    build();

            return new CommandResult.Builder<String>().badRequest(internalMessage).build();
        }

        var mpesaResponse = new ObjectMapper().readValue(mpesaClientResult.getId(), Map.class);
        var originatorConversationId = String.valueOf(mpesaResponse.get("OriginatorConversationID"));

        b2cRequest.setOriginatorConversationId(originatorConversationId);
        b2cRequest.setIsAccepted(true);

        b2cRequest = b2CRequestRepository.save(b2cRequest);

        return new CommandResult.Builder<String>().id(b2cRequest.getCorrelationId())
                .ok().build();
    }
}

