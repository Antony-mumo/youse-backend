package com.company.youse.services;

import com.company.youse.repositrories.ServiceProviderRepository;
import com.company.youse.repositrories.ServiceTypeRepository;
import com.company.youse.repositrories.UserRepository;
import com.company.youse.enums.RoleEnum;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.models.ServiceProvider;
import com.company.youse.models.ServiceType;
import com.company.youse.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {


    private final ServiceProviderRepository serviceProviderRepository;


    private final UserRepository userRepository;


    private final  UserService userService;


    private final ServiceTypeRepository serviceTypeRepository;

    public ResponseEntity<?> createServiceProvider(User newUser) throws IOException {
        User user = userRepository.findUserByEmail(newUser.getEmail());
        if (user == null )
            user = userRepository.findUserByPhoneNumber(newUser.getPhoneNumber());

        ServiceProvider serviceProvider = new ServiceProvider();

        // if  user is not found in database, proceed to create a new user in
        // database and add user as service provider
        //
        if(user == null){ // if no user is found
            // set the user's role as service provider
            newUser.setRole(RoleEnum.SERVICEPROVIDER);
            // add user to database
            newUser = userService.saveUser(newUser);
            serviceProvider.setAccountIsActive(true);
            serviceProvider.setUser(newUser);
            serviceProviderRepository.save(serviceProvider);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // else update user to be a serviceProvider too
        else{ // if user is found
            //check if user is serviceProvider already
            if(serviceProviderRepository.findById(user.getId()).isEmpty() ){ // if user is a service provider
                //return error
                throw new BadRequestException("email or phone number already exists");

            }else{ // if user is not a service provider
                //add serviceProvider role to user
                if(user.getRole().equals(RoleEnum.CUSTOMER)){ // if user is a customer
                    user.setRole(RoleEnum.BOTH_CUSTOMER_AND_SERVICEPROVIDER);
                }else{ // if user is not a service provider nor customer
                    user.setRole(RoleEnum.SERVICEPROVIDER);
                }
                //update user to set role to database
                userRepository.save(user);
                //set user found as service serviceProvider
                serviceProvider.setUser(user);
                // activate the user's service provider account
                serviceProvider.setAccountIsActive(true);
                // add the customer to database
                serviceProviderRepository.save(serviceProvider);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }


    /**
     * method used to set service provider skill
     * @param user
     * @param skill
     * @return
     */
    public ResponseEntity<?> setServiceProviderSkill(User user, String skill) {
        // get service Provider account related to user
        ServiceProvider serviceProvider = user.getServiceProvider() ;
        // if service provider is null, return a bad request http status
        if ( serviceProvider == null)
            throw new BadRequestException("User not allowed to make this request");
        // get a service type whose type is equal to the skill
        ServiceType serviceType = serviceTypeRepository.findTopByType(skill);
        // if skill does not exist, return a bad request http status
        if (serviceType == null)
            throw new BadRequestException("Skill does not exist");
        // get the service provider service list
        List<ServiceType> serviceTypeList = serviceProvider.getServiceTypes();
        // check if serviceTypeList contains the new serviceType we need to add
        for (ServiceType sT : serviceTypeList){
            if(sT.getType().equals(serviceType.getType())){
                // if service type was already added return an ok status
                return getServiceProviderSkills(user);
            }
        }

        // if execution gets here, then serviceTYpe needs to be added. Add serviceTYpe to serviceTypeList
        serviceTypeList.add(serviceType);
        // set service type list for service provider
        serviceProvider.setServiceTypes(serviceTypeList);
        // update service provider to update list in database
        serviceProviderRepository.save(serviceProvider);
        // return a successful response
        return getServiceProviderSkills(user);
    }

    /**
     * remove a skill from the set of skills a service provider offers
     * @param user
     * @param skill
     * @return
     */
    public ResponseEntity<?> removeServiceProviderSkill(User user, String skill) {
        // get service Provider account related to user
        ServiceProvider serviceProvider = user.getServiceProvider() ;
        // if service provider is null, return a bad request http status
        if ( serviceProvider == null)
            throw new BadRequestException("User not allowed to make this request");
        // get a service type whose type is equal to the skill
        ServiceType serviceType = serviceTypeRepository.findTopByType(skill);
        // if skill does not exist, return a bad request http status
        if (serviceType == null)
            throw new BadRequestException("Skill does not exist");
        // get the service provider service list
        List<ServiceType> serviceTypeList = serviceProvider.getServiceTypes();
        var list = new ArrayList<ServiceType>();
        list.addAll(serviceTypeList);
        // check if serviceTypeList contains the serviceType we need to delete
        for (ServiceType sT : list){
            if(sT.getType().equals(serviceType.getType())){
                // remove serviceType from list
                serviceTypeList.remove(sT);
                // update list in database to remove the service
                serviceProviderRepository.save(serviceProvider);
                // return an ok status
                continue;
            }
        }

        // return a successful response
        return getServiceProviderSkills(user);
    }

    public ResponseEntity<?> getServiceProviderSkills(User user) {
        // get services offered by service provider
        // get service Provider account related to user
        ServiceProvider serviceProvider = user.getServiceProvider() ;
        // if service provider is null, return a bad request http status
        if ( serviceProvider == null)
            throw new BadRequestException("User not allowed to make this request");
        // get the service provider service list
        List<ServiceType> serviceProviderServiceTypesList = serviceProvider.getServiceTypes();
        // get all the servicesTypes offered
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        // check if service provider has selected the service type
        for(ServiceType sT : serviceTypeList){
            // if selected, mark serviceType as selectedByServiceProvider
            if(serviceProviderServiceTypesList.contains(sT)){
                sT.setSelectedByServiceProvider(true);
            }
        }
        // return a successful response status with the list of serviceTypes
        return new ResponseEntity<>(serviceTypeList, HttpStatus.OK);
    }

    public ResponseEntity<?> updateServiceProviderSkills(User user, List<ServiceType> serviceTypeList) {
        // get service Provider account related to user
        ServiceProvider serviceProvider = user.getServiceProvider() ;
        // if service provider is null, return a bad request http status
        if ( serviceProvider == null)
            throw new BadRequestException("User not allowed to make this request");
        // get the service provider service list// get the service provider service list
        List<ServiceType> serviceProviderServiceTypesList = serviceProvider.getServiceTypes();
        // iterate the serviceTypeList, if serviceProvider has not selected service remove it from serviceProviderServiceTypesList else add it
        for(ServiceType sT : serviceTypeList){
            ServiceType dbVersionST = serviceTypeRepository.findTopByType(sT.getType());
            // confirm if the service really exists in database / validate the service
            if(dbVersionST != null){
                // if valid, delete or add it to users list
                if(sT.isSelectedByServiceProvider() && !serviceProviderServiceTypesList.contains(dbVersionST))
                    // if is selected by Service provider, add it to list
                    serviceProviderServiceTypesList.add(dbVersionST);
                else if(!sT.isSelectedByServiceProvider())
                    // else if it is not selected, remove it from the list
                    serviceProviderServiceTypesList.remove(dbVersionST);

            }
        }
        // finally update the service provider to push changes to database
        serviceProviderRepository.save(serviceProvider);

        return getServiceProviderSkills(user);
    }
}
