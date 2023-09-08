package com.company.youse.apiObjects;

import com.company.youse.enums.RoleEnum;
import com.company.youse.models.Avatar;
import com.company.youse.models.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileApiObject {

    private String fname;
    private String lname;
    private String email;
    private String phoneNumber;
    private String residence;
    private Integer idNumber;
    private Boolean isPhoneVerified;
    private Boolean isEmailVerified;

    public UserProfileApiObject(User user) {
        this.fname = user.getfName();
        this.lname = user.getlName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.residence = user.getResidence();
        this.idNumber = user.getIdNumber();
        this.isPhoneVerified = user.getPhoneVerified();
        this.isEmailVerified = user.getEmailVerified();
    }
}
