package com.company.youse.services;


import com.company.youse.repositrories.FirebaseTokensRepository;
import com.company.youse.repositrories.NotificationRepository;
import com.company.youse.repositrories.UserRepository;
import com.company.youse.apiObjects.FirebaseBroadCastResponseAPIObject;
import com.company.youse.apiObjects.FirebaseResponseAPIObject;
import com.company.youse.apiObjects.FirebaseTokenAPIObject;
import com.company.youse.controller.res.job.JobResponse;
import com.company.youse.enums.NotificationEnum;
import com.company.youse.enums.RoleEnum;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.*;
import com.company.youse.models.notificationData.AvailableServiceProvider;
import com.company.youse.thirdPartyServices.NotificationAPIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final FirebaseTokensRepository firebaseTokensRepository;


    private final Util util;


    private final NotificationAPIService notificationAPIService;


    private final NotificationRepository notificationRepository;


    private final UserRepository userRepository;

    public ResponseEntity<?> setFirebaseToken(User user, String accountType, FirebaseTokenAPIObject firebaseTokenAPIObject) {
        // ensure user is not null
        if (user == null)
            throw new MissingException("User not found");

        user = userRepository.findById(user.getId()).get();
        // get the firebase tokens for user here
        FirebaseTokens firebaseTokens = user.getFirebaseTokens();
        // if null, create a new FirebaseTokens object and set its owner to current user
        if (firebaseTokens == null) {
            firebaseTokens = new FirebaseTokens(user);
        }
        // switch statement to set get the account type and set the tokens respectively
        switch (accountType) {
            case "customer" :
                // TODO in future, enable customer to support multiple tokens
                firebaseTokens.setFirebaseCustomerFcmToken(firebaseTokenAPIObject.getToken());
                break;
            case "service-provider":
                // TODO in future, enable service provider to support multiple tokens
                firebaseTokens.setFirebaseServiceProviderFcmToken(firebaseTokenAPIObject.getToken());
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        // save firebase token object to database
        firebaseTokensRepository.save(firebaseTokens);

        // return successful http status here

        return new ResponseEntity<>(HttpStatus.OK);
    }


    public Boolean removeFirebaseTokenSuccessful(User user, String accountType) {
        // ensure user is not null
        if (user == null)
            return false;

        // get the firebase tokens for user here
        FirebaseTokens firebaseTokens = user.getFirebaseTokens();
        // if null, return successful
        if (firebaseTokens == null) {
            return true;
        }
        // switch statement to set get the account type and set the tokens respectively
        switch (accountType) {
            case "customer" :
                //if null, return true
                if(firebaseTokens.getFirebaseCustomerFcmToken() == null) {
                    return true;
                }
                firebaseTokens.setFirebaseCustomerFcmToken(null);
                break;
            case "service-provider":
                //if null, return true
                if(firebaseTokens.getFirebaseServiceProviderFcmToken() == null) {
                    return true;
                }
                firebaseTokens.setFirebaseServiceProviderFcmToken(null);
                break;
            default:
                return true;
        }
        // both customer and serviceProvider tokens are null, delete object
        if(firebaseTokens.getFirebaseCustomerFcmToken() == null && firebaseTokens.getFirebaseServiceProviderFcmToken() == firebaseTokens.getFirebaseCustomerFcmToken() ){
            firebaseTokensRepository.delete(firebaseTokens);
        }else {
            // update firebase token object to database
            firebaseTokensRepository.save(firebaseTokens);
        }
        // return true status here
        return true;
    }

    /**
     * Method sends a notification to the customer app
     * @param notification
     * @return
     */
    public boolean customerNotificationIsSuccessful(Notification notification){
        //set notification account type
        notification.setAccountType(RoleEnum.CUSTOMER);
        // Send the notification to the customer app
        FirebaseResponseAPIObject firebaseResponseAPIObject = notificationAPIService.sendNotification(notification, notification.getUser().getFirebaseTokens().getFirebaseCustomerFcmToken());
        // if firebaseTokenAPIObject is null, notification sending failed, return false
        if (firebaseResponseAPIObject == null || firebaseResponseAPIObject.isFailure() || !firebaseResponseAPIObject.isSuccess()) {
            // don't update in database as notification does not exist if id is null
            if(notification.getId() == null) {
                return false;
            }

            notification.setSuccessfullySentStatus(false);
            // update notification object in database
            notificationRepository.save(notification);

            return false;
        }
        //don't update in database as notification does not exist if id is null
        if(notification.getId() == null) {
            return true;
        }
        // update notification status in database
        // update sent status
        notification.setSuccessfullySentStatus(true);
        // set the multicast_id response from firebase
        notification.setMulticast_id(firebaseResponseAPIObject.getMulticast_id());
        // update notification object
        notificationRepository.save(notification);
        return true;
    }

    /**
     * Method sends a notification to the service provider app
     * @param notification
     * @return
     */
    public boolean serviceProviderNotificationIsSuccessful(Notification notification){
        //set notification account type
        notification.setAccountType(RoleEnum.SERVICEPROVIDER);
        // Send the notification to the Service provider app
        FirebaseResponseAPIObject firebaseResponseAPIObject = notificationAPIService.sendNotification(notification, notification.getUser().getFirebaseTokens().getFirebaseServiceProviderFcmToken());
        // if firebaseTokenAPIObject is null, notification sending failed, return false
        if (firebaseResponseAPIObject == null || firebaseResponseAPIObject.isFailure() || !firebaseResponseAPIObject.isSuccess()) {
            // don't update in database as notification does not exist if id is null
            if(notification.getId() == null) {
                return false;
            }

            notification.setSuccessfullySentStatus(false);
            // update notification object in database
            notificationRepository.save(notification);

            return false;
        }
        //don't update in database as notification does not exist if id is null
        if(notification.getId() == null) {
            return true;
        }
        // update notification status in database
        // update sent status
        notification.setSuccessfullySentStatus(true);
        // set the multicast_id response from firebase
        notification.setMulticast_id(firebaseResponseAPIObject.getMulticast_id());
        // update notification object
        notificationRepository.save(notification);
        return true;
    }

    /**
     * Method fetches all customer notifications for user
     * @param user
     * @return
     */
    public ResponseEntity<List<Notification>> getCustomerNotifications(User user){
        // return if customer does not exist
        if (user == null || user.getCustomer() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(notificationRepository.findAllByAccountTypeAndUserOrderByCreatedAtDesc(RoleEnum.CUSTOMER, user), HttpStatus.OK);
    }

    /**
     * Method fetches all service provider notifications for user
     * @param user
     * @return
     */
    public ResponseEntity<List<Notification>> getServiceProviderNotifications(User user){
        // return if service provider does not exist
        if (user == null || user.getServiceProvider() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(notificationRepository.findAllByAccountTypeAndUserOrderByCreatedAtDesc(RoleEnum.SERVICEPROVIDER, user), HttpStatus.OK);
    }

    /**
     * Method finds all unread notifications for customer/ user and marks them as read
     * @param user
     * @return
     */
    public ResponseEntity<?> markCustomerNotificationsRead(User user){
        // return if user does not exist
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //fetch all unread customer notifications
        // TODO write sql to improve performance
        List<Notification> unreadNotifications = user.getNotifications()
                // filter out where account type is customer only
                .stream().filter(o -> o.getAccountType().equals(RoleEnum.CUSTOMER)&& !o.isReadStatus())
                .collect(Collectors.toList());
        // loop through notifications updating each notification
        for (Notification notification : unreadNotifications){
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Method finds all unread notifications for service provider/ user and marks them as read
     * @param user
     * @return
     */
    public ResponseEntity<?> markServiceProviderNotificationsRead(User user){
        // return if user does not exist
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //fetch all unread customer notifications
        // TODO write sql to improve performance
        List<Notification> unreadNotifications = user.getNotifications()
                // filter out where account type is customer only
                .stream().filter(o -> o.getAccountType().equals(RoleEnum.SERVICEPROVIDER)&& !o.isReadStatus())
                .collect(Collectors.toList());
        // loop through notifications updating each notification
        for (Notification notification : unreadNotifications){
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @SneakyThrows
    public void jobApplicationNotificationToCustomer(JobApplication jobApplication) {
        // create a notification object
        Notification notification = new Notification(
                "Service Providers available", /* title to the notification */
                "A service provider is interested a job you posted'"+jobApplication.getJob().getJobTitle()+"'.", /* body to the notification */
                NotificationEnum.INFORMATION, /* notification type should be an enum */
                jobApplication.getJob().getCustomer().getUser() /* this is the user to receive the notification */
        );

        notification.setShouldNotify(false);
        notification.setDataType("serviceProvider");
        notification.setData( new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new AvailableServiceProvider(jobApplication)));

//        int numberOfJobApplicationsOnJob = jobApplication.getJob().getJobApplications().size();
//        //get the local date time
//        LocalDateTime latestDate = jobApplication.getJob().getJobApplications()
//                .stream()
//                // compare the time to get the latest
//                .max(comparing(JobApplication::getCreatedDateTime)).get()
//                .getCreatedDateTime();

        // check the number of jobApplications on the job
        // if it has only the current jobApplication
//            if(numberOfJobApplicationsOnJob != 1 ){
//                // notify customer service provider available or per 10 jobApplications, send notification
//                // if  job applications are more than one and time between last job application and now is more than an hour, send notification
//                if(numberOfJobApplicationsOnJob%10!=0  || ChronoUnit.MINUTES.between(LocalDateTime.now(), latestDate) < 59) {
//                    return;
//                }
//            }

        // if it does not fit the above, do not send notification
        // don't save information notification  to database

        // send notification to customer
        // TODO if you need to do something with successful sending of information
        if(customerNotificationIsSuccessful(notification)){
            return;
        }else{
            // TODO log error in out error reporter
            return;
        }
    }

    public void startedContractNotificationToServiceProvider(Contract contract) {
        // create a notification object
        Notification notification = new Notification(
                "You have a new Job", /* title to the notification */
                "Your application request to '"+contract.getJobApplication().getJob().getJobTitle()+"' has been accepted.", /* body to the notification */
                NotificationEnum.CONTRACT, /* notification type should be an enum */
                contract.getJobApplication().getServiceProvider().getUser() /* this is the user to receive the notification */
        );
        //save notification to database
        notificationRepository.save(notification);

        // send notification to service provider
        // TODO if you need to do something with successful sending of information
        if(serviceProviderNotificationIsSuccessful(notification)){
            return;
        }else{
            // TODO log error in out error reporter
            return;
        }
    }

    public void closedContractNotificationToServiceProvider(Contract contract) {
        // create a notification object
        Notification notification = new Notification(
                "Contract closed", /* title to the notification */
                "Your contract '"+contract.getJobApplication().getJob().getJobTitle()+"' has been closed.", /* body to the notification */
                NotificationEnum.CONTRACT, /* notification type should be an enum */
                contract.getJobApplication().getServiceProvider().getUser() /* this is the user to receive the notification */
        );
        //save notification to database
        notificationRepository.save(notification);

        // send notification to service provider
        // TODO if you need to do something with successful sending of information
        if(serviceProviderNotificationIsSuccessful(notification)){
            return;
        }else{
            // TODO log error in out error reporter
            return;
        }

    }

    public void closeContractRequestNotificationToCustomer(Contract contract) {
        // create a notification object
        Notification notification = new Notification(
                "Contract approval request", /* title to the notification */
                contract.getJobApplication().getServiceProvider().getUser().getfName()+" has requested payment for the job '"+contract.getJobApplication().getJob().getJobTitle()+"'.", /* body to the notification */
                NotificationEnum.CONTRACT, /* notification type should be an enum */
                contract.getJobApplication().getJob().getCustomer().getUser() /* this is the user to receive the notification */
        );
        //save notification to database
        notificationRepository.save(notification);

        // send notification to service provider
        // TODO if you need to do something with successful sending of information
        if(customerNotificationIsSuccessful(notification)){
            return;
        }else{
            // TODO log error in out error reporter
            return;
        }

    }

    @SneakyThrows
    public void postJobNotification(Job job, User user) {
        Notification notification = new Notification(
                "New Job!", /* title to the notification */
                String.format("Hi! There's an available %s job around you... you want in? ", job.getJobTitle()), /* body to the notification */
                NotificationEnum.JOB, /* notification type should be an enum */
                user /* this is the user to receive the notification */
        );

        notification.setShouldNotify(false);
        notification.setDataType("job");
        notification.setData( new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(JobResponse.copy(job)));

        notificationRepository.save(notification);

        notification.setAccountType(RoleEnum.SERVICEPROVIDER);

        // Send the notification to the Service provider app
        FirebaseBroadCastResponseAPIObject firebaseBroadCastResponseAPIObject = notificationAPIService.broadCast(notification
                , job.getServiceType().getType());

        // if firebaseTokenAPIObject is null, notification sending failed, return false
        if (firebaseBroadCastResponseAPIObject == null || firebaseBroadCastResponseAPIObject.isFailure() || !firebaseBroadCastResponseAPIObject.isSuccess()) {
            // don't update in database as notification does not exist if id is null
            if(notification.getId() == null) {
                return;
            }

            notification.setSuccessfullySentStatus(false);
            // update notification object in database
            notificationRepository.save(notification);

            return;
        }
        //don't update in database as notification does not exist if id is null
        if(notification.getId() == null) {
            return;
        }
        // update notification status in database
        // update sent status
        notification.setSuccessfullySentStatus(true);
        // set the multicast_id response from firebase
        notification.setMulticast_id(firebaseBroadCastResponseAPIObject.getMessage_id());
        // update notification object
        notificationRepository.save(notification);

    }
}
