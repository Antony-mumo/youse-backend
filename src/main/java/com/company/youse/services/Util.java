package com.company.youse.services;

import com.company.youse.repositrories.UserRepository;
import com.company.youse.models.User;
import com.company.youse.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.SplittableRandom;

@Service
@RequiredArgsConstructor
public class Util {

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	public static String getResponseObjStr(int code, String msg) {
		
		JSONObject response_obj = new JSONObject();
			try {
				if(code != 0) {
				response_obj.put("code", code);
				}
				if(msg != null)
					response_obj.put("msg", msg);
			} catch (JSONException e) {
				return "server error";
			}
		
		return response_obj+"";
	}
	
	public static JSONObject getResponseObj(int code, String msg) {
		
		JSONObject response_obj = new JSONObject();
		try {
			if(code != 0) {
				response_obj.put("code", code);
			}
			if(msg != null)
				response_obj.put("msg", msg);
		} catch (JSONException e) {
			return null;
		}
		return response_obj;
	}

	public static String language(String language) {
		String lang = null;
		
		if(StringUtils.isEmpty(language)) {
			 lang = "en";
			 return lang;
		}
		else {
			return language;	
		}
	}

	public User getUserFromToken(String jwtToken){
		//if jwtToke contains Bearer keyword, clean it
		jwtToken = jwtToken.replace("Bearer ", "");

		String username = jwtProvider.getUserNameFromJwtToken(jwtToken);
		if(isEmail(username)){
			return userRepository.findUserByEmail(username);
		}else{
			return userRepository.findUserByPhoneNumber(username);
		}
	}

	public boolean isEmail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	/**
	 * method checks if a phone number is valid or not
	 * @param phoneNumber
	 * @return
	 */
	public boolean isValidPhoneNumber(String phoneNumber) {
		// if phoneNumber is null return false
		if(phoneNumber.isEmpty()){
			return false;
		}
		// if it is not 12 digits long, return false
		if(phoneNumber.length() != 12){
			return false;
		}
		// if it does not start with 254, return false
		if(!phoneNumber.startsWith("254")){
			return false;
		}
		// if all digits are not numeric, return false
		// use apache commons isNumeric()
		if(!StringUtils.isNumeric(phoneNumber)){
			return false;
		}

		//default to true if it passes all tests
		return true;
	}


	/**
	 * Method returns a pseudoRandom integer value, to string,  with the specified length of digits
	 * @param lengthOfOTP
	 * @return
	 */
	public String generateOTP(final int lengthOfOTP){
		StringBuilder generatedOTP = new StringBuilder();
		SplittableRandom splittableRandom = new SplittableRandom();
		for (int i = 0 ; i< lengthOfOTP; i++){
			int randomNumber = splittableRandom.nextInt(0, 9);
			generatedOTP.append(randomNumber);
		}
		return  generatedOTP.toString();
	}
}
