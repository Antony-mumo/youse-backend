package com.company.youse.services;


import com.company.youse.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import javax.transaction.Transactional;

@Service
public class OTPService {


    private final Util util;


    //cache based on username and OPT MAX 8
    private static final Long EXPIRE_MINS = 30L;

    //declare google guava cache to hold generated OTP codes
    private LoadingCache<Long, String> otpCache;

    /**
     * constructor
     */
    public OTPService(Util util){
        super();
        this.util = util;
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<Long, String>() {
                    public String load(Long key) {
                        return null;
                    }
                });
    }

    /**
     * method generates OTP code and saves the code to the database. Rewrite the OTP if it exists
     * Using user id  as key
     * @param user
     * @return
     */
    public String generateOTPCode(User user){
        //generate otp code
        String otpCode = util.generateOTP(6);
        // store otp in cache memory, use userId as key
        otpCache.put(user.getId(), otpCode);
        // return the otpCode
        return otpCode;
    }

    /**
     * This method is used to return the OPT number against Key->Key values is user id
     * @param user
     * @return
     */
    public String getOTPCode(User user){
        try{
            return otpCache.get(user.getId());
        }catch (Exception e){
            return null;
        }
    }

    /**
     * This method is used to clear the OTP cached already
     * @param user
     */
    public void clearOTPCode(User user){
        otpCache.invalidate(user.getId());
    }
}
