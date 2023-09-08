package com.company.youse.models;

import com.company.youse.enums.JobApplicationStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobApplication")
public class JobApplication {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quotedPrice;

    private String addedInformation;

    // placeholder to receive jobId from http call in the controller
    @Transient
    private Long jobId;

    @ManyToOne
    @JoinColumn(name="serviceProvider_id", nullable=false)
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn(name="job_id", nullable=false)
    private Job job;

    @Enumerated(EnumType.STRING)
    private JobApplicationStatusEnum jobStatus;

    @OneToOne(mappedBy = "jobApplication", cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Contract contract;

    @CreationTimestamp
    private LocalDateTime createdDateTime;


    @Transient
    private ServiceProviderDetails serviceProviderDetails;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(BigDecimal quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

    @JsonIgnore
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }


    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public JobApplicationStatusEnum getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobApplicationStatusEnum jobStatus) {
        this.jobStatus = jobStatus;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getAddedInformation() {
        return addedInformation;
    }

    public void setAddedInformation(String addedInformation) {
        this.addedInformation = addedInformation;
    }

    public Long getJobId() {
        if(jobId != null)
            return jobId;

        return job.getId();
    }

    public ServiceProviderDetails getServiceProviderDetails() {
        return new ServiceProviderDetails(serviceProvider);
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Data
    private class ServiceProviderDetails{
        private Long id;
        private Integer rating;
        private Long jobsCount;
        private String fName, lName, phoneNumber;

        public ServiceProviderDetails(ServiceProvider serviceProvider) {
            User user = serviceProvider.getUser();
            this.id = serviceProvider.getId();
            this.fName = user.getfName();
            this.lName = user.getlName();
            this.phoneNumber = user.getPhoneNumber();
            this.rating = serviceProvider.getRating();
            this.jobsCount = serviceProvider.getJobsCount();
        }

    }
}
