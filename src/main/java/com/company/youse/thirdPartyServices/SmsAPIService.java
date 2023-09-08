package com.company.youse.thirdPartyServices;

import com.company.youse.apiObjects.ZetatelResponseAPIObject;
import com.company.youse.config.PropertiesFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsAPIService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PropertiesFetcher properties;


    private static final Logger LOGGER = LoggerFactory.getLogger(SmsAPIService.class);

    /**
     * Method send a form data to Zettatel with details of message and the contacts to send message to
     * @param message
     * @param contact
     * @return null if request fails or a valid object if successful
     */

    public ZetatelResponseAPIObject sendSMS(String message, String contact)  {
        // get the domain name from properties
        String ServerDomain = properties.getSmsServerDomain();
        // set the send sms api endpoint
        String ApiEndPoint = "/send";

        //First, set the Content-Type header to application/x-www-form-urlencoded.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Request parameters and other properties in a LinkedMultiValueMap.
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userid", properties.getSmsUserId());
        map.add("password", properties.getSmsPassword());
        map.add("msg", message);
        map.add("msgType", "text");
        // map.add("time", "2018-09-16 13:24:00");
        map.add("senderid", properties.getSmsSenderId());
//        map.add("test", "false");
        // map.add("dltEntityId", "xxxxxxxxxxxxx");
        // map.add("dltTemplateId", "xxxxxxxxxxxxx");
        map.add("duplicatecheck", "true");
        map.add("sendMethod", "quick");
        map.add("output", "json");
        map.add("mobile", contact);
        // build the Request using an HttpEntity instance
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        // connect to the REST service by calling restTemplate.postForEntity() on the Endpoint: /send
        ResponseEntity<ZetatelResponseAPIObject> response = restTemplate.postForEntity(
                ServerDomain+ApiEndPoint, request , ZetatelResponseAPIObject.class);
        // check for the response and return a valid object or null
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        else{
            LOGGER.error("Failed to load response object");
            return null;
        }
    }
}

