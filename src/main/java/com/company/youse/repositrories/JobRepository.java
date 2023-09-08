package com.company.youse.repositrories;

import com.company.youse.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByOrderByCreatedDateTimeDesc();



    /**
     *
     * @param jobId
     * @return count hired service providers
     */
    @Query(
            value = "SELECT count(*) as count " +
                    "FROM job j " +
                    "join jobApplication ja on j.id = ja.job_id " +
                    "right join contract c on ja.id = c.jobApplication_id " +
                    "where j.id = :jobId " +
                    ";"
            ,nativeQuery = true
    )
    ICount countHiredServiceProviders(Long jobId) ;

    @Query(
            value = "SELECT  " +
                    "jb.id as id, " +
                    "jb.jobTitle as jobTitle, " +
                    "jb.description as description, " +
                    "jb.location as location, " +
                    "jb.lattitude as lattitude, " +
                    "jb.longitude as longitude, " +
                    "jb.noOfWorkersNeeded as noOfWorkersNeeded, " +
                    "jb.jobStatus as jobStatus, " +
                    "jb.pricedAmount as pricedAmount, " +
                    "jb.serviceType_id as serviceType_id, " +
                    "jb.createdDateTime as createdDateTime, " +
                    "jb.customer_id as customer_id, " +
                    "(select count(ja.job_id) from jobApplication ja where ja.job_id = jb.id) as applicants " +
                    "FROM customer cust " +
                    "join job jb on cust.user_id = jb.customer_id " +
                    "where cust.user_id = :customerId  " +
                    "order by jobStatus asc, createdDateTime desc " +
                    "; ",
            nativeQuery = true
    )
    List<Job> getPostedJobsByCustomer(Long customerId);

    @Query(
            value = "SELECT " +
                    "    jb.id as id, " +
                    "    jb.jobTitle as jobTitle, " +
                    "    jb.description as description, " +
                    "    jb.location as location, " +
                    "    jb.lattitude as lattitude, " +
                    "    jb.longitude as longitude, " +
                    "    jb.noOfWorkersNeeded as noOfWorkersNeeded, " +
                    "    jb.jobStatus as jobStatus, " +
                    "    jb.pricedAmount as pricedAmount, " +
                    "    jb.serviceType_id as serviceType_id, " +
                    "    jb.createdDateTime as createdDateTime, " +
                    "    jb.customer_id as customer_id, " +
                    "    (select count(ja.job_id) from jobApplication ja where ja.job_id = jb.id) as applicants " +
                    "FROM job jb " +
                    "where  jb.jobStatus = 'POSTED' and  jb.id not in ( " +
                    "    SELECT j.id as id " +
                    "    FROM job j " +
                    "             join jobApplication ja on j.id = ja.job_id and ja.serviceProvider_id = :userId " +
                    ") and jb.serviceType_id in ( " +
                    "    SELECT shs.service_id from serviceProvider_has_service shs  " +
                    "    where shs.serviceProvider_id = :userId " +
                    "    ) " +
                    ";"
            ,nativeQuery = true
    )
    List<Job> getAvailableJobsFor(Long userId);


    @Query(
           value = "SELECT count(*) > 0 as status  FROM serviceProvider s " +
                   "join jobApplication ja on ja.serviceProvider_id = s.user_id " +
                   "join job jb on jb.id = ja.job_id " +
                   "where jb.id = :jobId " +
                   " and s.user_id = :userId " +
                   ";",
            nativeQuery = true
    )
    IBooleanStatus serviceProviderHasAppliedForJob(Long userId, Long jobId);

}
