package com.company.youse.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "serviceProvider")
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "serviceProvider_has_service",
            joinColumns = @JoinColumn(name = "serviceProvider_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceType> serviceTypes;


    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "int(4) default 0")
    private Long jobsCount;

    @Column(columnDefinition = "double(8,2) default 0")
    private BigDecimal totalAmountEarned;

    private String location;

    @Column(columnDefinition = "double(8,2) default 0")
    private BigDecimal runningDebt;

    @Column(columnDefinition = "int(2) default 0")
    private Integer rating;

    @OneToMany(mappedBy="serviceProvider")
    private List<JobApplication> jobApplications;


    @Column(columnDefinition = "tinyint(1) default true")
    private Boolean isAccountIsActive;


    @OneToMany(cascade =  CascadeType.REMOVE, mappedBy = "serviceProvider", fetch = FetchType.LAZY)
    private List<Document> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ServiceType> getServices() {
        return serviceTypes;
    }

    public void setServices(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getJobsCount() {
        return jobsCount;
    }

    public void setJobsCount(Long jobsCount) {
        this.jobsCount = jobsCount;
    }

    public BigDecimal getTotalAmountEarned() {
        return totalAmountEarned;
    }

    public void setTotalAmountEarned(BigDecimal totalAmountEarned) {
        this.totalAmountEarned = totalAmountEarned;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getRunningDebt() {
        return runningDebt;
    }

    public void setRunningDebt(BigDecimal runningDebt) {
        this.runningDebt = runningDebt;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonIgnore
    public List<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Boolean getAccountIsActive() {
        return isAccountIsActive;
    }

    public void setAccountIsActive(Boolean accountIsActive) {
        isAccountIsActive = accountIsActive;
    }

    @JsonIgnore
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
