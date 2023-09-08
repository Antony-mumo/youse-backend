package com.company.youse.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "service")
public class ServiceType {

    public ServiceType(){ }

    public ServiceType(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
    }

    public ServiceType(String type) {
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
//
//    @ManyToMany(mappedBy = "serviceTypes")
//    private List <ServiceProvider> serviceProviders;

    private String type;

    private String subtype;

    @Transient
    private boolean selectedByServiceProvider;
//
//    @OneToMany(mappedBy="serviceType")
//    private List<Job> jobs;


    @Override
    public String toString() {
        return "ServiceType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @JsonIgnore
//    public List<ServiceProvider> getServiceProviders() {
//        return serviceProviders;
//    }
//
//    public void setServiceProviders(List<ServiceProvider> serviceProvider) {
//        this.serviceProviders = serviceProvider;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
//
//    @JsonIgnore
//    public List<Job> getJobs() {
//        return jobs;
//    }
//
//    public void setJobs(List<Job> jobs) {
//        this.jobs = jobs;
//    }

    public boolean isSelectedByServiceProvider() {
        return selectedByServiceProvider;
    }

    public void setSelectedByServiceProvider(boolean selectedByServiceProvider) {
        this.selectedByServiceProvider = selectedByServiceProvider;
    }


    @Override
    public boolean equals(Object obj) {
        if(Objects.isNull(obj))
            return false;

        if(obj instanceof ServiceType){
            var o = (ServiceType) obj;
            return o.getId().equals(getId());
        }
        return false;
    }
}
