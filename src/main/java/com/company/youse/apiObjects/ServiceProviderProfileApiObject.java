package com.company.youse.apiObjects;

import com.company.youse.models.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceProviderProfileApiObject {

    private String fname;
    private String lname;
    private String email;
    private String phoneNumber;
    private String residence;
    private Integer idNumber;
    private Boolean isPhoneVerified;
    private Boolean isEmailVerified;
    private Integer rating;
    private BigDecimal totalAmountEarned;
    private String location;

    public ServiceProviderProfileApiObject(User user) {
        this.fname = user.getfName();
        this.lname = user.getlName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.residence = user.getResidence();
        this.idNumber = user.getIdNumber();
        this.isPhoneVerified = user.getPhoneVerified();
        this.isEmailVerified = user.getEmailVerified();
        this.rating = user.getServiceProvider().getRating();
        this.totalAmountEarned = user.getServiceProvider().getTotalAmountEarned();
        this.location = user.getServiceProvider().getLocation();

    }
}
