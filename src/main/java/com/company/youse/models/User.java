package com.company.youse.models;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.company.youse.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "user")
public class User {

    public User(){

    }

    /**
     * Constructor from User object
     * @param user
     * For Spring Security
     */
    public User(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public String toString() {
        return "User{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fName;
    @NotNull
    private String lName;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @NotNull
    private String residence;

    private Integer idNumber;

    @Column(columnDefinition="tinyint(1) default false")
    private Boolean isPhoneVerified;

    @Column(columnDefinition="tinyint(1) default false")
    private Boolean isEmailVerified;

    @OneToMany(cascade =  CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Session> sessions;


    @OneToMany(cascade =  CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private FirebaseTokens firebaseTokens;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Avatar avatar;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ServiceProvider serviceProvider;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Customer customer;

    @Transient
    private MultipartFile profilePicData;

    @Transient
    private String profilePicture;

    @Transient
    private String refresh_token;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
    @JsonIgnore
    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    @JsonIgnore
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public MultipartFile getProfilePicData() {
        return profilePicData;
    }

    public void setProfilePicData(MultipartFile profilePicData) {
        this.profilePicData = profilePicData;
    }

    @JsonIgnore
    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }
    @JsonIgnore
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonIgnore
    public FirebaseTokens getFirebaseTokens() {
        return this.firebaseTokens;
    }

    public void setFirebaseTokens(FirebaseTokens firebaseTokens) {
        this.firebaseTokens = firebaseTokens;
    }

    public String getProfilePicture() {
        if(getAvatar() != null){
            return getAvatar().getName();
        }
        return "";
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    @JsonIgnore
    public Boolean getPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }
    @JsonIgnore
    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
