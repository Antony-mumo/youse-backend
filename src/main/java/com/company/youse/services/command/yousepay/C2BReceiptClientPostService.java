package com.company.youse.services.command.yousepay;


import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.repositrories.yousepay.C2BTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class C2BReceiptClientPostService extends CommandBaseService<C2BReceiptClientPostCommand, String> {

    private final C2BTransactionRepository c2BTransactionBody;

    public C2BReceiptClientPostService(C2BTransactionRepository c2BTransactionBody) {
        this.c2BTransactionBody = c2BTransactionBody;
    }

    @Override
    public CommandResult<String> execute(C2BReceiptClientPostCommand command) {
        var c2BTransaction = command.getC2BTransaction();

        var httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        httpHeaders.set("cache-control", "no-cache");

        String result;

        try {
            var httpEntity = new HttpEntity<>(new ObjectMapper().writer()
                    .withDefaultPrettyPrinter().writeValueAsString(command.getC2BTransaction()), httpHeaders);
            var restTemplate = new RestTemplate();

            result = restTemplate.postForObject(command.getOrgShortCode().getC2bReceiptUrl()
                    , httpEntity, String.class);

        }catch (Exception e){
            e.printStackTrace();

            c2BTransaction.setSynced(false);
            c2BTransaction.setSyncMessage(e.getLocalizedMessage());

            c2BTransactionBody.save(c2BTransaction);

            InternalMessage internalMessage = InternalMessage.builder()
                    .message(e.getLocalizedMessage())
                    .code("400").build();

            return new CommandResult.Builder<String>().badRequest(internalMessage).build();
        }

        c2BTransaction.setSynced(true);
        c2BTransaction.setSyncMessage("SUCCESS");

        c2BTransactionBody.save(c2BTransaction);

        return new CommandResult.Builder<String>().id(result).g(result).build();
    }
}
