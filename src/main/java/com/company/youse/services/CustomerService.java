package com.company.youse.services;

import com.company.youse.repositrories.CustomerRepository;
import com.company.youse.repositrories.JobRepository;
import com.company.youse.repositrories.UserRepository;
import com.company.youse.enums.RoleEnum;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.models.Customer;
import com.company.youse.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final JobRepository jobRepository;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final UserService userService;


    public ResponseEntity<?> createCustomer(User newUser) throws IOException {
        User user = userRepository.findUserByEmail(newUser.getEmail());
        if (user == null )
            user = userRepository.findUserByPhoneNumber(newUser.getPhoneNumber());

        Customer customer = new Customer();


        // if user is not found in database, proceed to create a new user in
        // database and add user as service provider
        //
        if(user == null){
            newUser.setRole(RoleEnum.CUSTOMER);
            // add user to database
            newUser = userService.saveUser(newUser);
            customer.setUser(newUser);
            customer.setAccountIsActive(true);
            customerRepository.save(customer);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        // else update user to be a customer too
        else{
        //check if user is customer already
            if(user.getCustomer() != null ){ // is user is a customer
                //return error is a customer
                System.out.println("user Id : " + user.getId());
                throw new BadRequestException("email or phone number already exists");
            }else{ // if user is not a customer
                //add customer role to user
                if(user.getRole().equals(RoleEnum.SERVICEPROVIDER)){ // if user is a service provider
                    user.setRole(RoleEnum.BOTH_CUSTOMER_AND_SERVICEPROVIDER);
                }else{ // if user is not a service provider nor customer
                    user.setRole(RoleEnum.CUSTOMER);
                }
                //update user to set role to database
                userRepository.save(user);
                //set user found as customer
                customer.setUser(user);
                // activate the user's customer account
                customer.setAccountIsActive(true);
                // add the customer to database
                customerRepository.save(customer);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }
}
