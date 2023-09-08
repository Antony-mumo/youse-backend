package com.company.youse.services;

import com.company.youse.repositrories.*;
import com.company.youse.apiObjects.FeedbackAPIObject;
import com.company.youse.models.views.CustomerContractView;
import com.company.youse.models.views.ServiceProviderContractView;
import com.company.youse.enums.ContractStatusEnum;
import com.company.youse.enums.JobApplicationStatusEnum;
import com.company.youse.enums.JobStatusEnum;
import com.company.youse.enums.RoleEnum;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.errorHandler.ConflictException;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.*;
import com.company.youse.repositrories.views.CustomerContractViewRepository;
import com.company.youse.repositrories.views.ServiceProviderContractViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final CustomerRepository customerRepository;

    private final ServiceProviderRepository serviceProviderRepository;

    private final JobRepository jobRepository;

    private final ServiceProviderContractViewRepository serviceProviderContractViewRepository;

    private final ContractRepository contractRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final JobApplicationRepository jobApplicationRepository;

    private final CustomerContractViewRepository customerContractViewRepository;

    private final NotificationService notificationService;

    private final UserRepository userRepository;

    public ResponseEntity<?> postJob(Job job, User user){
        //Obtain customer
        Customer customer = customerRepository.findById(user.getId()).get();

        if(customer != null){
            // some logic to check if a job is valid goes here
            job.setCustomer(customer);

            //get serviceType from database
            ServiceType serviceType = serviceTypeRepository.findTopByType(job.getServiceType().getType());
            //set jobStatus to posted
            job.setJobStatus(JobStatusEnum.POSTED);
            if(serviceType != null){
                job.setServiceType(serviceType);
                // set unknown if location is not given
                if(job.getLocation() == null){
                    job.setLocation("unknown,unknown,unknown");
                }
                jobRepository.save(job);
                System.out.println(job);

                notificationService.postJobNotification(job, userRepository.findById(user.getId()).get());
            }else {
                throw new BadRequestException("Service not offered");
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            throw new BadRequestException("Permission denied");
        }
    }

    public ResponseEntity<?> getAvailableJobs(User user){
        //Obtain serviceProvider
        ServiceProvider serviceProvider = serviceProviderRepository.findById(user.getId()).get();
        //check if service provider exists
        if(serviceProvider != null){
            // add serviceProvider to those who have applied
            return new ResponseEntity<>(jobRepository.getAvailableJobsFor(serviceProvider.getId()), HttpStatus.OK);
        }else {
            throw new BadRequestException("Permission denied");
        }
    }

    public ResponseEntity<?> applyJob(JobApplication jobApplication, User user) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(user.getId()).get();
        //check if service provider exists
        if(serviceProvider != null){
            // add serviceProvider to those who have applied
            jobApplication.setServiceProvider(serviceProvider);
            Job job = jobRepository.findById(jobApplication.getJobId()).get();
            if(job != null){
                var jA = jobApplicationRepository.findById(jobApplication.getJobId()).get();
                if (jA != null) {
                    var contract = jA.getContract();
                    if (contract != null){
                        var active = contract.getStatus();
                        if (active != null && active.equals(ContractStatusEnum.CLOSED))
                            throw new BadRequestException("Job already selected");
                    }
                }
                
                if(jobApplicationRepository.findTopByServiceProviderAndJob(serviceProvider, job).isPresent()) {
                    throw new BadRequestException("You have already applied for this job");
                }else{
                    jobApplication.setJob(job);
                    jobApplication.setJobStatus(JobApplicationStatusEnum.APPLICATION);
                    //if job and serviceProvider are available, save job application request
                    jobApplication = jobApplicationRepository.save(jobApplication);
                    //TODO Notify customer someone has sent a job application
                    notificationService.jobApplicationNotificationToCustomer(jobApplication);
                    hireServiceProvider(jobApplication.getId(),user);
                    //return success response
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }else throw new MissingException("Job not found");
        }else {
            throw new BadRequestException("Permission denied");
        }
    }

    public ResponseEntity<?> getAllJobs(User user) {
        return new ResponseEntity<>(jobRepository.findByOrderByCreatedDateTimeDesc(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAppliedJobs(User user) {
        //Obtain serviceProvider
        ServiceProvider serviceProvider = serviceProviderRepository.findById(user.getId()).get();
        //check if service provider exists
        if(serviceProvider != null){
            // add serviceProvider to those who have applied
            return new ResponseEntity<>(serviceProvider.getJobApplications(), HttpStatus.OK);
        }else {
            throw new BadRequestException("Permission denied");
        }
    }

    public ResponseEntity<?> getPostedJobs(User user) {
        //Obtain customer
        Customer customer = user.getCustomer();
        //check if service provider exists
        if(customer != null){
            // get all jobs posted by the customer
            return new ResponseEntity<>(jobRepository.getPostedJobsByCustomer(customer.getId()), HttpStatus.OK);
        }else {
            throw new BadRequestException("Permission denied");
        }
    }


    public ResponseEntity<?> getJobApplications(User user, Long jobId) {
        //Obtain customer
        Customer customer = user.getCustomer();
        //check if service provider exists
        if(customer != null){
            Job job = jobRepository.findById(jobId).get();
            //check if job exists in database
            if(job != null){
                // if job exists,
                // get all jobApplications related to job
                return new ResponseEntity<>(job.getJobApplications(), HttpStatus.OK);
            }else {
                throw new MissingException("Job not found");
            }
        }else {
            throw new BadRequestException("Permission denied");
        }
    }

    public ResponseEntity<?> hireServiceProvider(Long jobApplicationId, User user) {
        // Retrieve jobApplication from database
        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId).get();
        // check if it's customer who posted job that is making the request
        Job job = jobApplication.getJob();
        // compare details in authenticated user to details from job
        Long userIdFromJob =  job.getCustomer().getUser().getId();
        if(userIdFromJob == user.getId()){ // if details match, then authenticated user did post the job
            Contract contract = jobApplication.getContract();
            if(contract == null) {
                // TODO in the future, include a promoCode, if customer made a request with one
                contract = new Contract(jobApplication);
                // set the contract status
                contract.setStatus(ContractStatusEnum.ACTIVE);
                contractRepository.save(contract);
                // To find number of hiredServiceProviders
                Long hiredServiceProviders = jobRepository.countHiredServiceProviders(job.getId()).getCount();
                //check if job has gotten the total number of service providers needed
                if(job.getNoOfWorkersNeeded() == hiredServiceProviders) {
                    job.setJobStatus(JobStatusEnum.HIRED);
                    //update job_status to hired
                    jobRepository.save(job);
                }
                // TODO notify service provider they have been hired
                notificationService.startedContractNotificationToServiceProvider(contract);
                // return a successful response
                return new ResponseEntity<>(contract, HttpStatus.OK);
            }
            throw new ConflictException("Contract exists");
        } // else throw exception
        else{
            throw new BadRequestException("Permission denied");
        }
    }


    public ResponseEntity<?> endContract(Long contractId, User user) {
        //check if contract exists in the database/ validate contract
        Contract contract = contractRepository.findById(contractId).get();
        Job job = contract.getJobApplication().getJob();
        if (contract != null){
            // check if it's customer who posted job that is making the request
            // compare details in authenticated user to details from job
            Long userIdFromJob =  job.getCustomer().getUser().getId();
            if(userIdFromJob == user.getId()){ // if details match, then authenticated user did post the
                // TODO check if contract was running before ending
                // set the contract status
                contract.setStatus(ContractStatusEnum.CLOSED);
                contractRepository.save(contract);
                // set job status to closed
                job.setJobStatus(JobStatusEnum.CLOSED);
                jobRepository.save(job);
                // TODO initiate payment if its m-pesa integration or card payment
                // Notify both ServiceProvider their contract has been ended
                notificationService.closedContractNotificationToServiceProvider(contract);
                // return a successful response
                return new ResponseEntity<>(HttpStatus.OK);
            } // else throw exception
            else{
                throw new BadRequestException("Permission denied");
            }
        }else{
            throw new MissingException("Contract not found");
        }

    }

    public ResponseEntity<?> addFeedback(FeedbackAPIObject feedback, User user) {

        //check if contract exists in the database/ validate contract
        Contract contract = contractRepository.findById(feedback.getContractId()).get();
        if (contract != null){
            // TODO ensure the contract status is closed to enable posting of feedbacks
            // check if it's customer who posted job / serviceProvider who did the job that is making the request
            // compare details in authenticated user to details from job
            Long userIdFromContract =  contract.getJobApplication().getJob().getCustomer().getUser().getId();
            Long serviceProviderFromContract = contract.getJobApplication().getServiceProvider().getUser().getId();
            if(userIdFromContract == user.getId() || serviceProviderFromContract == user.getId()){ // if details match, then authenticated user did post the
                // set the contract feedback
                contract.setCustomerFeedbackToYouse(feedback.getCustomerFeedbackToYouse());
                contract.setServiceProviderFeedbackToYouse(feedback.getServiceProviderFeedbackToYouse());
                contract.setServiceProviderRatingToYouse(feedback.getServiceProviderRatingToYouse());

                contract.setFeedbackToCustomer(feedback.getFeedbackToCustomer());
                contract.setRatingToCustomer(feedback.getRatingToCustomer());

                contract.setFeedbackToServiceProvider(feedback.getFeedbackToServiceProvider());
                contract.setRatingToServiceProvider(feedback.getRatingToServiceProvider());

                // update contract in database to save feedback
                contractRepository.save(contract);

                // TODO update the review on both service provider and customer profile, can be here or added a cron task

                return new ResponseEntity<>(HttpStatus.OK);
            } // else throw exception
            else{
                throw new BadRequestException("Permission denied");
            }
        }else{
            throw new MissingException("Contract not found");
        }
    }

    public ResponseEntity<?> submitContractForApproval(Long contractId, User user) {
        //check if contract exists in the database/ validate contract
        Contract contract = contractRepository.findById(contractId).get();
        Job job = contract.getJobApplication().getJob();
        if (contract != null){
            // check if it's service provider who did job that is making the request
            // compare details in authenticated user to details from job
            Long userIdFromJob =  contract.getJobApplication().getServiceProvider().getUser().getId();
            if(userIdFromJob == user.getId()){ // if details match, then authenticated user did do the job
                // set the contract status
                contract.setStatus(ContractStatusEnum.PENDING); // pending status means the job is done awaiting approval,
                contractRepository.save(contract);
                // set the job status to pending
                job.setJobStatus(JobStatusEnum.PENDING);
                jobRepository.save(job);
                // TODO notify client serviceProvider has completed job and is requesting for pay
                notificationService.closeContractRequestNotificationToCustomer(contract);
                // return a successful response
                return new ResponseEntity<>(HttpStatus.OK);
            } // else throw exception
            else{
                throw new BadRequestException("Permission denied");
            }
        }else{
            throw new MissingException("Contract not found");
        }
    }

    /**
     * method gets the contracts of the certain user
     * @param user
     * @param role is the user role, method supports "customer" and "service-provider" roles only
     * @param contractStatus this is the contractStatus
     * @return
     */
    public ResponseEntity<?> getContracts(User user, String role, String contractStatus) {
        // if user is null throw a bad request
        if(user==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //get the role of the user
        RoleEnum userRole = user.getRole();
        //initialize an empty contracts list
        List<?> contracts;
        //switch case to switch between the different roles
            switch (role){
                case "customer":
                    if (userRole == RoleEnum.CUSTOMER || userRole == RoleEnum.BOTH_CUSTOMER_AND_SERVICEPROVIDER) {
                            return new ResponseEntity<List<CustomerContractView>>(customerContractViewRepository.findAllByCustomerId(user.getCustomer().getId()), HttpStatus.OK);
                    }
                    break;
                case "service-provider":
                    if (userRole == RoleEnum.SERVICEPROVIDER || userRole == RoleEnum.BOTH_CUSTOMER_AND_SERVICEPROVIDER) {
                            return new ResponseEntity<List<ServiceProviderContractView>>(serviceProviderContractViewRepository.findAllByServiceProviderId(user.getServiceProvider().getId()), HttpStatus.OK);
                    }
                    break;
                default:
                    throw new BadRequestException("url mismatch");
            }

            throw  new BadRequestException("user not allowed to perform this action");
    }

    public ResponseEntity<?> checkApplied(Long jobId, User user) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(user.getId()).get();
        if(serviceProvider != null){ //check if serviceProvider exists
            Job job = jobRepository.findById(jobId).get(); //if exists, check if job exists
            if(job != null ) {
                // check if service provider applied for job
                if(jobRepository.serviceProviderHasAppliedForJob(serviceProvider.getId(), job.getId()).getStatus()){
                    //if so, return error
                    throw new ConflictException("job application exists");
                };
                return new ResponseEntity<>(HttpStatus.OK);
            }
            throw new MissingException("Job not found");//return error, job not found
        }
        throw new BadRequestException("Permission denied");//return error unauthorized/ bad request
    }
}
