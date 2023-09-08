package com.company.youse.services;


import com.company.youse.repositrories.UserRepository;
import com.company.youse.apiObjects.OTPCodeAPIObject;
import com.company.youse.apiObjects.ZetatelResponseAPIObject;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.errorHandler.ServerErrorException;
import com.company.youse.models.User;
import com.company.youse.thirdPartyServices.SmsAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SMSService {

    private final OTPService otpService;

    private final UserRepository userRepository;

    private final Util util;

    private final SmsAPIService smsAPIService;

    /**
     * method sends OTP code message to user. According to status of sent message, it returns true of false
     * @param user
     * @return
     */
    public boolean hasSentOTPCodeSuccessfully(User user) {
        // check if/ ensure user has a phone number
        if(user.getPhoneNumber() != null) {
            //validate user phone number
            if(util.isValidPhoneNumber(user.getPhoneNumber())){
                //generate OTP code and save it to database
                String OTP = otpService.generateOTPCode(user);

                //compose OTP message
                String OTPMessage = "[Youse] Your security code is: "+OTP+". It expires in 30 minutes. Do not share this code with anyone.";

                //call sendSMS api
                ZetatelResponseAPIObject zetatelResponseAPIObject = smsAPIService.sendSMS(OTPMessage,user.getPhoneNumber());
                //return response, as either true or false
                if(zetatelResponseAPIObject != null && zetatelResponseAPIObject.getStatus().equals("success")){
                    return true;
                }else {
                    return false;
                }
            };
        }

        throw new ServerErrorException("an error occurred, contact youse"); // error, no phone number
    }


    /**
     * Obtains original otp code and compares it to one submitted by user, if matches set user phone number as verified
     * @param user
     * @param otpCode
     * @return
     */
    public ResponseEntity<?> confirmOTP(User user, String otpCode) {
        // obtain the otp from cache
        String originalOtpCode = otpService.getOTPCode(user);
        // ensure originalOtpCode is not null, and that it is same as sent otpCode
        if( originalOtpCode != null && originalOtpCode.equals(otpCode)){
            //remove otp from cache
            otpService.clearOTPCode(user);
            //set phoneVerified status to true
            user.setPhoneVerified(true);
            // update user in database
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new BadRequestException("Otp code is invalid or expired");
    }

    /**
     * method used to set a new phone number for user, or verify users phone number
     * @param user
     * @param otpCodeAPIObject
     * @return
     */
    public ResponseEntity<?> sendOtpCode(User user, OTPCodeAPIObject otpCodeAPIObject) {
        if (user == null){
            throw new MissingException("user not found");
        }
        //get the phone number remove spaces and any + sign on it
        String phoneNumber = otpCodeAPIObject.getPhoneNumber().replace(" ", "").replace("+","");

        // check if phone number is null, if not null, update phone number and set its status to not verified
        if(phoneNumber != null){
            //check validity of phone number
            if(util.isValidPhoneNumber(phoneNumber)) {
                // set new phone number for user and set its status to not verified
                user.setPhoneNumber(phoneNumber);
                user.setPhoneVerified(false);
                // update user in database
                userRepository.save(user);
            }else{
                // throw a phone number invalid error
                throw new BadRequestException("invalid phone number");
            }
        }
        // send OTPCode to phone number
        if(hasSentOTPCodeSuccessfully(user))
            return  new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
