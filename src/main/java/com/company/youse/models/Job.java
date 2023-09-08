package com.company.youse.models;

import com.company.youse.enums.JobStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "job")
public class Job {

    public Job(String jobTitle, String description, String location, BigDecimal lattitude, BigDecimal longitude, Long noOfWorkersNeeded, JobStatusEnum jobStatus, BigDecimal pricedAmount, ServiceType serviceType, Customer customer) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.location = location;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.noOfWorkersNeeded = noOfWorkersNeeded;
        this.jobStatus = jobStatus;
        this.pricedAmount = pricedAmount;
        this.serviceType = serviceType;
        this.customer = customer;
    }

    public Job() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;


    @Column(precision = 10, scale = 8)
    private BigDecimal lattitude;

    @Column(precision = 10, scale = 8)
    private BigDecimal longitude;

    private Long noOfWorkersNeeded;

    @Enumerated(EnumType.STRING)
    private JobStatusEnum jobStatus;

    private BigDecimal pricedAmount;

    @ManyToOne
    @JoinColumn(name="serviceType_id", nullable=false)
    private ServiceType serviceType;

    @OneToMany(mappedBy="job")
    private List<JobApplication> jobApplications;

    @CreationTimestamp
    private Date createdDateTime;

    //link to customer
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    @Transient
    private CustomerDetails customerDetails ;

    // TODO find a way to set this to transient, column not needed in database
    private Long applicants;

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", noOfWorkersNeeded='" + noOfWorkersNeeded + '\'' +
                ", jobStatus=" + jobStatus +
                ", pricedAmount=" + pricedAmount +
                ", serviceType=" + serviceType +
                ", createdDateTime=" + createdDateTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getNoOfWorkersNeeded() {
        return noOfWorkersNeeded;
    }

    public void setNoOfWorkersNeeded(Long noOfWorkersNeeded) {
        this.noOfWorkersNeeded = noOfWorkersNeeded;
    }

    public JobStatusEnum getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatusEnum jobStatus) {
        this.jobStatus = jobStatus;
    }

    public BigDecimal getPricedAmount() {
        return pricedAmount;
    }

    public void setPricedAmount(BigDecimal pricedAmount) {
        this.pricedAmount = pricedAmount;
    }

    @JsonIgnore
    public List<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getLattitude() {
        return lattitude;
    }

    public void setLattitude(BigDecimal lattitude) {
        this.lattitude = lattitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public CustomerDetails getCustomerDetails() {
        return new CustomerDetails(customer.getId(),customer.getUser().getfName(), customer.getUser().getlName(), customer.getRating());
    }

    public Long getApplicants() {
            return applicants;
    }

    public void setApplicants(Long applicants) {
        this.applicants = applicants;
    }

    //Customer class object to be sent in responses
    private class CustomerDetails{
        private Long id;
        private Integer rating;
        private String fName, lName;

        public CustomerDetails(Long id, String fName, String lName, Integer rating) {
            this.id = id;
            this.fName = fName;
            this.lName = lName;
            this.rating = rating;
        }

        public Long getId() {
            return id;
        }

        public Integer getRating() {
            return rating;
        }

        public String getfName() {
            return fName;
        }

        public String getlName() {
            return lName;
        }
    }
}
