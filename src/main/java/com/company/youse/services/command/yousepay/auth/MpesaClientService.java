package com.company.youse.services.command.yousepay.auth;

import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class MpesaClientService extends CommandBaseService<MpesaClientCommand, String> {

    private final MpesaAuthenticationService mpesaAuthenticationService;

    @Autowired
    public MpesaClientService(MpesaAuthenticationService mpesaAuthenticationService) {
        this.mpesaAuthenticationService = mpesaAuthenticationService;
    }

    @Override
    public CommandResult<String> execute(MpesaClientCommand command) {
        var mpesaAuthResult = mpesaAuthenticationService
                .decorate(new MpesaAuthenticationCommand(command.getAppKey(), command.getAppSecret()));

        if (mpesaAuthResult.getBase().isFailed()){
            return mpesaAuthResult;
        }

        var authToken = mpesaAuthResult.getId();

        var httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization","Bearer ".concat(authToken));
        httpHeaders.set("cache-control", "no-cache");

        String response;

        try {

            var httpEntity = new HttpEntity<>(new ObjectMapper().writer()
                    .withDefaultPrettyPrinter().writeValueAsString(command.getRequestObject()), httpHeaders);
            var restTemplate = new RestTemplate();
            log.info(new ObjectMapper().writeValueAsString(httpEntity));
             response =  restTemplate.postForObject(command.getUrl(), httpEntity, String.class);

        }catch (JsonProcessingException e){
            e.printStackTrace();
            return new CommandResult.Builder<String>().internalServerFailure().build();
        }catch (Exception e){
            e.printStackTrace();
            return new CommandResult.Builder<String>()
                    .clientError(HttpStatus.MULTI_STATUS,e.getLocalizedMessage()).build();
        }

        return new CommandResult.Builder<String>().id(response).g(response).ok().build();
    }
}
