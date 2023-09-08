package com.company.youse.controller;

import com.company.youse.apiObjects.OTPCodeAPIObject;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.JobApplication;
import com.company.youse.models.User;
import com.company.youse.services.SMSService;
import com.company.youse.services.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Transactional
@RequiredArgsConstructor
public class SMSController {

    private final Util util;

    private final SMSService smsService;

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/sms/otp/request", method = RequestMethod.POST)
    public ResponseEntity<?> sendOTPCode(HttpServletRequest request, @RequestBody OTPCodeAPIObject otpCodeAPIObject) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return smsService.sendOtpCode(user,otpCodeAPIObject);
    }


    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/sms/otp/confirm", method = RequestMethod.POST)
    public ResponseEntity<?> confirmOTPCode(HttpServletRequest request, @RequestBody OTPCodeAPIObject otpCodeAPIObject) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if(user!=null){
            return smsService.confirmOTP(user, otpCodeAPIObject.getOtpCode());
        }
        else
            throw new MissingException("user not found");
    }
}
