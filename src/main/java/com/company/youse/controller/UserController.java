package com.company.youse.controller;

import com.company.youse.repositrories.UserRepository;
import com.company.youse.apiObjects.FirebaseTokenAPIObject;
import com.company.youse.apiObjects.PasswordApiObject;
import com.company.youse.config.PropertiesFetcher;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.User;
import com.company.youse.services.NotificationService;
import com.company.youse.services.UserService;
import com.company.youse.services.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Transactional
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    private final PropertiesFetcher properties;

    private final Util util;

    private final UserRepository userRepository;

    private final NotificationService notificationService;

    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(HttpServletRequest request, @RequestBody  User user) throws IOException {
        System.out.println("signing up user new");
        System.out.println(user);
        if(properties.getCustomAuthToken().equals(request.getHeader("Authorization"))) {
            return userService.createUser(user);
        }
        else
            throw new BadRequestException("Request not authenticated");
    }


    /**
     * method logins user to their account / creates an active session for a user in the server
     * does not check the role type of the user
     * method is used for general login, but to log in a specific user type, do not use this method
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/api/oauth2/token", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(HttpServletRequest request, @RequestBody User user) {

            String password = user.getPassword();
            String email = user.getEmail();
            String refresh_token = user.getRefresh_token();
        System.out.println("signing in: "+ email +" "+password);
        System.out.println(user);
                if(properties.getCustomAuthToken().equals(request.getHeader("Authorization"))){
                    return userService.authenticate(email, password);
                }else{
                    return userService.renewJwt(refresh_token, request.getHeader("Authorization"));
                }
    }

    /**
     * method logins user to their account / creates an active session for a user in the server
     * use this method to ensure user logging in is a valid customer or a valid service-provider
     * @param accountType is the specific account type we need to authenticate user into, can either be customer or service-provider
     * @param request
     * @param user is a user object, containing either an email and password, or a refresh token to renew the access token
     * @return
     */
    @RequestMapping(value = "/api/{accountType}/oauth2/token", method = RequestMethod.POST)
    public ResponseEntity<?> signInWithAccountType(HttpServletRequest request ,@PathVariable(value="accountType") String accountType, @RequestBody User user) {

        String password = user.getPassword();
        String email = user.getEmail();
        String refresh_token = user.getRefresh_token();
        System.out.println("signing in: "+ email +" "+password);
        System.out.println(user);
        if(properties.getCustomAuthToken().equals(request.getHeader("Authorization"))){
            return userService.authenticate(email, password, accountType);
        }else{
            return userService.renewJwt(refresh_token, request.getHeader("Authorization"));
        }
    }

    @RequestMapping(value = "/api/user/check-email/{account}", method = RequestMethod.GET)
    public ResponseEntity<?> validateEmail(HttpServletRequest request,@PathVariable(value="account") String accountType, @RequestParam String email) {
        if(properties.getCustomAuthToken().equals(request.getHeader("Authorization"))){
            return userService.checkEmail(email, accountType);
        }else
            throw new BadRequestException("Request not authenticated");
    }

    @RequestMapping(value = "/api/user/{account}/profile", method = RequestMethod.GET)
    public ResponseEntity<?> profile(HttpServletRequest request,@PathVariable(value="account") String accountType) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return userService.getProfile(user, accountType);
    }


    @RequestMapping(value = "/api/user/{account}/public/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getProfilePublic(HttpServletRequest request,@PathVariable(value="account") String accountType, @RequestParam Long id) {
        if(properties.getCustomAuthToken().equals(request.getHeader("Authorization"))){
            User user = userRepository.findById(id).get();
            return userService.getProfile(user, accountType);
        }else
            throw new BadRequestException("Request not authenticated");
    }


    @RequestMapping(value = "/api/user/password/confirm", method = RequestMethod.POST)
    public ResponseEntity<?>  confirmOldPassword(HttpServletRequest request, @RequestBody PasswordApiObject passwordApiObject) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if(user != null){
            return userService.confirmOldPassword(user, passwordApiObject);
        }else{
            throw new MissingException("User not found");
        }
    }


    @RequestMapping(value = "/api/user/password/change", method = RequestMethod.POST)
    public ResponseEntity<?>  changePassword(HttpServletRequest request, @RequestBody PasswordApiObject passwordApiObject) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if(user != null){
            return userService.changePassword(user, passwordApiObject);
        }else{
            throw new MissingException("User not found");
        }
    }


    /**
     * Method logs out a user from the server by deleting the refresh token and therefore once session expires it can not be renewed
     * user has to create another session by logging in if session has been removed
     * @param request
     * @param accountType
     * @param session
     * @return
     */
    @RequestMapping(value = "/api/user/{accountType}/logout", method = RequestMethod.GET)
    public ResponseEntity<?> endSession(HttpServletRequest request,@PathVariable(value="accountType") String accountType, @RequestParam Long session) {
        // Obtain user from the request bearer token
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        // if is a valid user/ if user is not null
        if (user != null) {
            // remove session
            return userService.endSession(user,session, accountType);
        } else
            // throw a user not found error
            throw new BadRequestException("user not found");
    }


    /**
     * saves firebase token to identify users device
     * @param request
     * @param accountType
     * @param firebaseTokenAPIObject
     * @return
     */
    @RequestMapping(value = "/api/user/{accountType}/firebase/token", method = RequestMethod.POST)
    public ResponseEntity<?>  setFirebaseToken(HttpServletRequest request, @PathVariable(value="accountType") String accountType, @RequestBody FirebaseTokenAPIObject firebaseTokenAPIObject) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if(user == null)
            throw new MissingException("User not found");

            return notificationService.setFirebaseToken(user, accountType, firebaseTokenAPIObject);

    }


    /**
     * get all notifications for a customer
     * @param request
     * @param accountType
     * @return
     */
    @RequestMapping(value = "/api/user/{accountType}/notifications", method = RequestMethod.GET)
    public ResponseEntity<?> getNotifications(HttpServletRequest request, @PathVariable(value="accountType") String accountType) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if(user == null)
            throw new MissingException("User not found");
        switch (accountType){
            case "customer" :
                if(user.getCustomer() != null)
                    return notificationService.getCustomerNotifications(user);
                break;
            case "service-provider":
                if (user.getServiceProvider()!=null)
                    return notificationService.getServiceProviderNotifications(user);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    /**
     * mark all notifications as read
     * @param request
     * @param accountType
     * @return
     */
    @RequestMapping(value = "/api/user/{accountType}/notifications/read", method = RequestMethod.GET)
    public ResponseEntity<?> markReadNotifications(HttpServletRequest request, @PathVariable(value="accountType") String accountType) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if(user == null)
            throw new MissingException("User not found");
        switch (accountType){
            case "customer" :
                return notificationService.markCustomerNotificationsRead(user);
            case "service-provider":
                return notificationService.markServiceProviderNotificationsRead(user);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }

    }



}


