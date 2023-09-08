package com.company.youse.models;

import com.company.youse.enums.ContractStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
public class Contract {

    public Contract(){}


    public Contract(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
        this.amountClientHasToPay = jobApplication.getQuotedPrice();
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;



    @OneToOne
    @MapsId
    @JoinColumn(name = "jobApplication_id")
    private JobApplication jobApplication;

    //either debated or the amount serviceProvider has offered.
    private BigDecimal amountClientHasToPay;

    private String promoCode;

    private ContractStatusEnum status;

    private String feedbackToServiceProvider;

    private String feedbackToCustomer;

    private String serviceProviderFeedbackToYouse;

    private String customerFeedbackToYouse;

    private Integer ratingToServiceProvider;

    private Integer ratingToCustomer;

    private Integer serviceProviderRatingToYouse;

    private Integer customerRatingToYouse;

    @CreationTimestamp
    private LocalDateTime createdDateTime;


    public Long getId() {
        return id;
    }

    @JsonIgnore
    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public BigDecimal getAmountClientHasToPay() {
        return amountClientHasToPay;
    }

    public void setAmountClientHasToPay(BigDecimal amountClientHasToPay) {
        this.amountClientHasToPay = amountClientHasToPay;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ContractStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ContractStatusEnum status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public String getFeedbackToServiceProvider() {
        return feedbackToServiceProvider;
    }

    public void setFeedbackToServiceProvider(String feedbackToServiceProvider) {
        this.feedbackToServiceProvider = feedbackToServiceProvider;
    }

    @JsonIgnore
    public String getFeedbackToCustomer() {
        return feedbackToCustomer;
    }

    public void setFeedbackToCustomer(String feedbackToCustomer) {
        this.feedbackToCustomer = feedbackToCustomer;
    }

    @JsonIgnore
    public String getServiceProviderFeedbackToYouse() {
        return serviceProviderFeedbackToYouse;
    }

    public void setServiceProviderFeedbackToYouse(String serviceProviderFeedbackToYouse) {
        this.serviceProviderFeedbackToYouse = serviceProviderFeedbackToYouse;
    }

    @JsonIgnore
    public String getCustomerFeedbackToYouse() {
        return customerFeedbackToYouse;
    }

    public void setCustomerFeedbackToYouse(String customerFeedbackToYouse) {
        this.customerFeedbackToYouse = customerFeedbackToYouse;
    }

    @JsonIgnore
    public Integer getRatingToServiceProvider() {
        return ratingToServiceProvider;
    }

    public void setRatingToServiceProvider(Integer ratingToServiceProvider) {
        this.ratingToServiceProvider = ratingToServiceProvider;
    }

    @JsonIgnore
    public Integer getRatingToCustomer() {
        return ratingToCustomer;
    }

    public void setRatingToCustomer(Integer ratingToCustomer) {
        this.ratingToCustomer = ratingToCustomer;
    }

    @JsonIgnore
    public Integer getServiceProviderRatingToYouse() {
        return serviceProviderRatingToYouse;
    }

    public void setServiceProviderRatingToYouse(Integer serviceProviderRatingToYouse) {
        this.serviceProviderRatingToYouse = serviceProviderRatingToYouse;
    }

    @JsonIgnore
    public Integer getCustomerRatingToYouse() {
        return customerRatingToYouse;
    }

    public void setCustomerRatingToYouse(Integer customerRatingToYouse) {
        this.customerRatingToYouse = customerRatingToYouse;
    }
}
