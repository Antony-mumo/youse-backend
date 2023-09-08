package com.company.youse.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("prop")
public class PropertiesFetcher {
    private String customAuthToken;
    private String avatarToken;

    //Zetattel sms api details
    private String smsServerDomain;
    private String smsUserId;
    private String smsPassword;
    private String smsSenderId;

    //firebase api details
    private String firebaseServerkey;
    private String firebaseDomain;

}
