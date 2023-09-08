package com.company.youse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "int(2) default 0")
    private Integer rating;

    @Column(columnDefinition = "tinyint(1) default true")
    private Boolean isAccountIsActive;

    @OneToMany(mappedBy="customer")
    private List<Job> jobs;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonIgnore
    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Boolean getAccountIsActive() {
        return isAccountIsActive;
    }

    public void setAccountIsActive(Boolean accountIsActive) {
        isAccountIsActive = accountIsActive;
    }
}
