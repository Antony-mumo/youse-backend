package com.company.youse.services;

import com.company.youse.repositrories.UserRepository;
import com.company.youse.apiObjects.CustomerProfileApiObject;
import com.company.youse.apiObjects.PasswordApiObject;
import com.company.youse.apiObjects.ServiceProviderProfileApiObject;
import com.company.youse.enums.RoleEnum;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.errorHandler.ConflictException;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.Session;
import com.company.youse.models.User;
import com.company.youse.security.JwtProvider;
import com.sun.net.httpserver.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final SessionService sessionService;

    private final AvatarService avatarService;

    private final NotificationService notificationService;

    private final Util util;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User findByEmail(String email) {
        return new User();
    }

    public ResponseEntity<?> createUser(User user) throws IOException {
        if(user.getPassword() != null && user.getEmail() != null && user.getfName() != null && user.getlName() != null) {

//            MultipartFile profilePic = user.getProfilePicData();

            if(userRepository.findUserByEmail(user.getEmail()) != null){
                throw new BadRequestException("User with email already exists");
            }

            String password = encodePassword(user.getPassword());
            user.setPassword(password);
            //set email verified and phone verified to false by default
            user.setEmailVerified(false);
            user.setPhoneVerified(false);
            userRepository.save(user);

//            if(profilePic!= null){
//                System.out.println("Original Image Byte Size - " + profilePic.getBytes().length);
//                System.out.println("saving profile pic");
//                Avatar avatar = avatarService.createAvatar(user, user.getfName(), profilePic.getContentType(), profilePic.getBytes());
//                System.out.println(avatar);
//            }

            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        }
        else{
            throw new BadRequestException("Incomplete signup details provided");
        }
    }

    public User saveUser(User user) throws IOException {
        if(user.getPassword() != null && user.getEmail() != null && user.getfName() != null && user.getlName() != null) {

//            MultipartFile profilePic = user.getProfilePicData();

            if(userRepository.findUserByEmail(user.getEmail()) != null){
                throw new BadRequestException("User with email already exists");
            }

            String password = encodePassword(user.getPassword());
            user.setPassword(password);
            userRepository.save(user);

//            if(profilePic!= null){
//                System.out.println("Original Image Byte Size - " + profilePic.getBytes().length);
//                System.out.println("saving profile pic");
//                Avatar avatar = avatarService.createAvatar(user, user.getfName(), profilePic.getContentType(), profilePic.getBytes());
//                System.out.println(avatar);
//            }

            return user;
        }
        else{
            throw new BadRequestException("Incomplete signup details provided");
        }
    }

    private String encodePassword(String password) {
         // Strength set as 12
        return encoder.encode(password);
    }

    /**
     * method logs in a user to the server / creates an active session of a user on the server
     * method does not check the account type, use the method below if checking of account type is needed
     * @param userName is a phone number of email, login using phone numbers not yet implemented
     * @param password
     * @return
     */
    public ResponseEntity<Map<String, Object>> authenticate(String userName, String password) {
        //find user by email or phone
        User user = userRepository.findUserByEmailOrPhoneNumber(userName, userName);

        System.out.println(user);
        if(user != null){
            if(encoder.matches(password, user.getPassword())){
                Map<String, Object > userObj  = new HashMap<>();
                userObj.put("userId", user.getId());
                userObj.put("fName", user.getfName());
                userObj.put("lName", user.getlName());
                if(user.getAvatar()!=null)
                    userObj.put("avatar", user.getAvatar().getName());
                userObj.put("email", user.getEmail());
                userObj.put("phoneNumber", user.getPhoneNumber());
                userObj.put("isNumberVerified", user.getPhoneVerified());
                userObj.put("isEmailVerified", user.getEmailVerified());

                Map<String, Object> response = new HashMap<>();
//                response.put("userName",  user.getEmail());
//                response.put("userid",  user.getId());
                //generate refresh token
                response.put("user",userObj);
                Session session = sessionService.generateRefreshToken(user);
                response.put("refresh_token", session.getRefreshToken() );
                // send session Id to user. Is used for logging out
                response.put("session_id", session.getId());
                //generate jwt token
                response.put("access_token", jwtProvider.generateJwtTokenWithUsername(user.getEmail()));
                response.put("token_type", "Bearer");
                //expires in an hour but let user know it expires in 50 minutes
                response.put("expires_in" , 50 * 60 * 1000);
                //send details to user
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                throw new BadRequestException("wrong password or email");
            }
        } else{
            throw new MissingException("wrong password or email");
        }
    }


    /**
     * method used by client application and service provider application to ensure one logs in to the correct account to avoid
     * feature access issues.
     * i.e. if a service provider logs into a client app, they can not post jobs unless service provider has both customer and service
     * provider roles
     * method used the method above, authentication to login a user to the server, only that it does check the roles
     * @param email
     * @param password
     * @param accountType
     * @return
     */
    public ResponseEntity<?> authenticate(String email, String password, String accountType) {
        //find user by email or phone
        User user = userRepository.findUserByEmailOrPhoneNumber(email, email);
        if (user != null) {
            // a switch statement to perform login based on the account type, checks if a user has specified role
            switch (accountType) {
                case "customer": {
                    // if user has no customer role, throw an error, user not found
                    if (user.getRole() == RoleEnum.BOTH_CUSTOMER_AND_SERVICEPROVIDER || user.getRole() == RoleEnum.CUSTOMER) {
                        // user signed up as a customer, authenticate user
                        return authenticate(email, password);
                    }
                    break;
                }
                case "service-provider": {
                    // if user has no service provider role, throw an error, service provider not found
                    if (user.getRole() == RoleEnum.BOTH_CUSTOMER_AND_SERVICEPROVIDER || user.getRole() == RoleEnum.SERVICEPROVIDER) {
                        // user signed up as a service provider, authenticate user
                        return authenticate(email, password);
                    }
                    break;
                }
                default:    // send an error here, account type not supported
                    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }
        // send an error here, user does not exist
            throw new MissingException("wrong password or email");
    }

    public ResponseEntity<?> renewJwt(String refresh_token, String authorization) {
        System.out.println("refresh_token: "+ refresh_token);
        System.out.println("authorization: "+ authorization);
        String userNameFromRefreshToken = jwtProvider.getUserNameFromJwtToken(refresh_token);
        String userNameFromAuthToken = jwtProvider.getUserNameFromExpiredJwtToken(authorization.replace("Bearer ", ""));

        System.out.println("refresh_token: "+userNameFromRefreshToken+" "+ refresh_token);
        System.out.println("authorization: "+userNameFromAuthToken+" "+authorization);

        User user = null;
        //validate both refresh_token and auth_token are for same user
        if(userNameFromAuthToken.equals(userNameFromRefreshToken)){
            //find user by email or phone
            user = userRepository.findUserByEmailOrPhoneNumber(userNameFromRefreshToken, userNameFromRefreshToken);
        }

        System.out.println(user);

        if(user != null ){
            Map<String, Object> response = new HashMap<>();

            //to-do authenticate jwt token

            //generate refresh token
            response.put("refresh_token", sessionService.renewRefreshToken(user, refresh_token));
            //generate new jwt token
            response.put("access_token", jwtProvider.generateJwtTokenWithUsername(user.getEmail()));
            response.put("token_type", "Bearer");
            //expires in an hour but let user know it expires in 50 minutes
            response.put("expires_in" , 3000000);
            //send details to user
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new BadRequestException("Bad request");

    }

    public ResponseEntity<?> checkEmail(String email, String accountType) {
        //find user by email or phone
        User user = userRepository.findUserByEmailOrPhoneNumber(email, email);
        System.out.println("finding user with email "+email);
        Map<String, Object> response = new HashMap<>();
        if(user != null){
            switch (accountType) {
                case "customer":
                    if(user.getCustomer() != null){  // send a conflict error, email exists
                        throw new ConflictException("Email already exists");
                    }
                    break;
                case "service-provider":
                    if(user.getServiceProvider() != null){  // send a conflict error, email exists
                        throw new ConflictException("Email already exists");
                    }
                    break;
                case "staff": // send an error here, account exists
                    throw new ConflictException("Email already exists");
                default:    // send an error here, account type not supported
                    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }

            response.put("email", user.getEmail()); // send email address from database
            response.put("signedUp", "true");   // user has a youse account, either staff / sp account, should proceed to signin
            //send details to user
            return new ResponseEntity<>(response, HttpStatus.OK);

        }else{
            if(util.isEmail(email)){
                System.out.println("User does not exist in system");
                response.put("email", email); // send email address from database
                response.put("signedUp", "false");   // user has no youse account, should proceed with signup process
                //send details to user
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                throw new BadRequestException("Invalid email");
            }
        }
    }

    public ResponseEntity<?> getProfile(User user, String accountType) {
        if (user != null){
            switch (accountType) {
                case "customer":
                    if(user.getCustomer() == null){  // send a conflict error, email exists
                        throw new MissingException("User not found");
                    }else{
                        return new ResponseEntity<>(new CustomerProfileApiObject(user), HttpStatus.OK);
                    }
                case "service-provider":
                    if(user.getServiceProvider() == null){  // send a conflict error, email exists
                        throw new MissingException("User not found");
                    }else{
                        return new ResponseEntity<>(new ServiceProviderProfileApiObject(user), HttpStatus.OK);
                    }
                case "staff": // send an error here, account exists
                    throw new MissingException("User not found");
                default:    // send an error here, account type not supported
                    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }else{
            throw new MissingException("User not found");
        }
    }

    public ResponseEntity<?> confirmOldPassword(User user, PasswordApiObject passwordApiObject) {
        if(encoder.matches(passwordApiObject.getOldPassword(), user.getPassword())){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            throw new BadRequestException("Wrong password");
        }
    }


    public ResponseEntity<?> changePassword(User user, PasswordApiObject passwordApiObject) {
        //confirm old password is a match
        if(encoder.matches(passwordApiObject.getOldPassword(), user.getPassword())){
            //remove all active sessions for user
            sessionService.removeAllSessionsFor(user);
            //change password for user to new password
            String password = encodePassword(passwordApiObject.getNewPassword());
            user.setPassword(password);
            //update password in database
            userRepository.save(user);
            //create a new session for current session and send session to user
            //initiate a login
            return authenticate(user.getEmail(), passwordApiObject.getNewPassword());
        }else{
            throw new BadRequestException("Wrong password");
        }
    }

    /**
     * on logging in a session is created in the session table, on logout, delete the session
     * @param user
     * @param sessionId
     * @param accountType
     * @return
     */
    public ResponseEntity<?> endSession(User user, Long sessionId, String accountType) {
        // remove fcm token
        notificationService.removeFirebaseTokenSuccessful(user, accountType);
        // try removing session with the refresh token
        if(sessionService.hasRemovedSessionWithId(sessionId, user)){ // if successfully removed
            // user has been logged out, return a successful request here
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //throw an error here, removing session error
        throw new BadRequestException("an error occurred on logout");
    }
}
