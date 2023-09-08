package com.company.youse.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "firebaseTokens")
public class FirebaseTokens {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String firebaseCustomerFcmToken;

    private String firebaseServiceProviderFcmToken;

    @CreationTimestamp
    private Date createdAt;


    public FirebaseTokens() {
    }

    public FirebaseTokens(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirebaseCustomerFcmToken() {
        return firebaseCustomerFcmToken;
    }

    public void setFirebaseCustomerFcmToken(String firebaseCustomerFcmToken) {
        this.firebaseCustomerFcmToken = firebaseCustomerFcmToken;
    }

    public String getFirebaseServiceProviderFcmToken() {
        return firebaseServiceProviderFcmToken;
    }

    public void setFirebaseServiceProviderFcmToken(String firebaseServiceProviderFcmToken) {
        this.firebaseServiceProviderFcmToken = firebaseServiceProviderFcmToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
