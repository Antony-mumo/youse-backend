package com.company.youse.services.command.yousepay.auth;

import com.company.youse.errorHandler.AuthenticationFailureException;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Type;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.utils.AppUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class MpesaAuthenticationService extends CommandBaseService<MpesaAuthenticationCommand, String> {

    private final String authenticationURL;

    private final RestTemplate restTemplate;

    public MpesaAuthenticationService( @Value("${mpesa.client_credentials_url}") String authenticationURL
            ,RestTemplate restTemplate) {
        this.authenticationURL = authenticationURL;
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    @Override
    public CommandResult<String> execute(MpesaAuthenticationCommand command) {

        if (AppUtils.Objects.isEmpty(command.getAppKey()) || AppUtils.Objects.isEmpty(command.getAppSecret()) ){

            var internalMessage = InternalMessage
                    .builder()
                    .type(Type.FAILURE)
                    .code("400")
                    .isTechnical(false)
                    .message("mpesa app key or app secret not found")
                    .build();

            return new CommandResult.Builder<String>().badRequest(internalMessage).build();
        }

        var key_secret = command.getAppKey() + ":" + command.getAppSecret();

        var bytes = key_secret.getBytes();
        var encoder = new Base64(true);
        var encoded = encoder.encodeToString(bytes).replaceAll("\\s+","");

        var httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization","Basic ".concat(encoded));
        httpHeaders.set("cache-control", "no-cache");
        var httpEntity = new HttpEntity<>(httpHeaders);

        var typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};

        try{

            log.info(httpEntity.toString());

            var authResponse = restTemplate.exchange(
                    authenticationURL,
                    HttpMethod.GET,
                    httpEntity,
                    typeRef);

            var aToken = authResponse.getBody();

            if (Objects.isNull(aToken)) throw new AuthenticationFailureException();

            var token = String.valueOf(Objects.requireNonNull(authResponse.getBody().get("access_token")));

            return new CommandResult.Builder<String>().id(token).g(token).ok().build();

        }catch (Exception e){
            log.error(e.getLocalizedMessage());

            var internalMessage = InternalMessage
                    .builder()
                    .type(Type.FAILURE)
                    .code("500")
                    .isTechnical(false)
                    .message(e.getLocalizedMessage())
                    .build();

            return new CommandResult.Builder<String>().internalServerFailure().message(internalMessage).build();
        }
    }
}
