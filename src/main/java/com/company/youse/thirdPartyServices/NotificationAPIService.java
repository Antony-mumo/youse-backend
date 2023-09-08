package com.company.youse.thirdPartyServices;

import com.company.youse.apiObjects.FirebaseBroadCastResponseAPIObject;
import com.company.youse.apiObjects.FirebaseResponseAPIObject;
import com.company.youse.config.PropertiesFetcher;
import com.company.youse.models.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class NotificationAPIService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PropertiesFetcher properties;


    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationAPIService.class);

    /**
     * Method send a post data to Firebase with details of title, fcm code, body, and some data
     * @param notification
     * @return null if request fails or a valid object if successful
     */

    public FirebaseResponseAPIObject sendNotification(Notification notification, String fcmToken)  {
        // get the domain name from properties
        String ServerDomain = properties.getFirebaseDomain();
        // set the send sms api endpoint
        String ApiEndPoint = "/fcm/send";

        // if fcm token does not exist, exit method and return null
        if(fcmToken == null)
            return null;

        //First, set the Content-Type header to application/x-www-form-urlencoded.
        HttpHeaders headers = new HttpHeaders();
        // set the content type
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set the authorization key for firebase
        headers.set("Authorization", "key="+properties.getFirebaseServerkey());

        // Request parameters and other properties in a LinkedMultiValueMap.
        JSONObject map= new JSONObject();
        map.put("to", fcmToken);
        map.put("password", properties.getSmsPassword());
        map.put("collapse_key" , "type_a");

        // prepare the notification object

        if(notification.isShouldNotify()){
            JSONObject notificationObj= new JSONObject();
            notificationObj.put("body" , notification.getBody());
            notificationObj.put("title" , notification.getTitle());
            map.put("notification" , notificationObj);
        }

        if(!Objects.isNull(notification.getData())){
            // prepare the data to send with the notification
            JSONObject dataOjb= new JSONObject();
            dataOjb.put(notification.getDataType(), notification.getData());
            map.put("data" ,dataOjb);
        }



        // build the Request using an HttpEntity instance
        HttpEntity<String> request = new HttpEntity<String>(map.toString(), headers);

        // connect to the REST service by calling restTemplate.postForEntity() on the Endpoint: /fcm/send
        ResponseEntity<FirebaseResponseAPIObject> response = restTemplate.postForEntity(
                ServerDomain+ApiEndPoint, request , FirebaseResponseAPIObject.class);
        // check for the response and return a valid object or null
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        else{
            LOGGER.error("Failed to load response object on sending notification");
            return null;
        }
    }


    /**
     * Method send a post data to Firebase with details of title, fcm code, body, and some data
     * @param notification
     * @return null if request fails or a valid object if successful
     */

    public FirebaseBroadCastResponseAPIObject broadCast(Notification notification, String topic) throws JsonProcessingException {
        // get the domain name from properties
        String ServerDomain = properties.getFirebaseDomain();
        // set the send sms api endpoint
        String ApiEndPoint = "/fcm/send";


        // if fcm token does not exist, exit method and return null
        if(topic == null)
            return null;

        //First, set the Content-Type header to application/x-www-form-urlencoded.
        HttpHeaders headers = new HttpHeaders();
        // set the content type
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set the authorization key for firebase
        headers.set("Authorization", "key="+properties.getFirebaseServerkey());

        // Request parameters and other properties in a LinkedMultiValueMap.
        JSONObject map= new JSONObject();
        map.put("to", "/topics/".concat(topic));
        map.put("password", properties.getSmsPassword());
        map.put("collapse_key" , "type_a");

        // prepare the notification object

        if(notification.isShouldNotify()){
            JSONObject notificationObj= new JSONObject();
            notificationObj.put("body" , notification.getBody());
            notificationObj.put("title" , notification.getTitle());
        }

        if(!Objects.isNull(notification.getData())){
            // prepare the data to send with the notification
            JSONObject dataOjb= new JSONObject();
            dataOjb.put(notification.getDataType(), notification.getData());
            map.put("data" ,dataOjb);
        }



        // build the Request using an HttpEntity instance
        HttpEntity<String> request = new HttpEntity<String>(map.toString(), headers);

        // connect to the REST service by calling restTemplate.postForEntity() on the Endpoint: /fcm/send
        ResponseEntity<FirebaseBroadCastResponseAPIObject> response = restTemplate.postForEntity(
                ServerDomain+ApiEndPoint, request , FirebaseBroadCastResponseAPIObject.class);
        // check for the response and return a valid object or null
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        else{
            LOGGER.error("Failed to load response object on broadcasting message");
            return null;
        }
    }
}

