package com.company.youse.services.command.yousepay;


import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class ClientService extends CommandBaseService<ClientCommand, String> {

    @Override
    public CommandResult<String> execute(ClientCommand command) {

        var httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        httpHeaders.set("cache-control", "no-cache");

        String response;

        try {

            var httpEntity = new HttpEntity<>(new ObjectMapper().writer()
                    .withDefaultPrettyPrinter().writeValueAsString(command.getClientResponse()), httpHeaders);
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

