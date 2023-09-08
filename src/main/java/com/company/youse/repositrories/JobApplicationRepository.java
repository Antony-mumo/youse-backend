package com.company.youse.repositrories;

import com.company.youse.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {


    /**
     * Method checks if a certain service provider has applied for a certain job
     * @param serviceProvider
     * @param job
     * @return
     */
    Optional<JobApplication> findTopByServiceProviderAndJob(ServiceProvider serviceProvider, Job job);

}
