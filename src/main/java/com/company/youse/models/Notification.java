package com.company.youse.models;

import com.company.youse.enums.NotificationEnum;
import com.company.youse.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String title;

    private String body;

    private NotificationEnum notificationType;

    @Column(columnDefinition="tinyint(1) default true")
    private boolean successfullySentStatus;

    private String multicast_id;

    @Column(columnDefinition="tinyint(1) default false")
    private boolean readStatus;

    private RoleEnum accountType;

    @CreationTimestamp
    private Date createdAt;


    @Column(columnDefinition="tinyint(1) default true")
    private boolean shouldNotify = true;

    @Column(columnDefinition = "TEXT")
    private String data;

    private String dataType;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Notification(String title, String body, NotificationEnum notificationType, User user) {
        this.notificationType = notificationType;
        this.title = title;
        this.body = body;
        this.user = user;
    }

    public Notification() {
    }

    @JsonIgnore
    public RoleEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(RoleEnum accountType) {
        this.accountType = accountType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NotificationEnum getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationEnum notificationType) {
        this.notificationType = notificationType;
    }

    @JsonIgnore
    public boolean isSuccessfullySentStatus() {
        return successfullySentStatus;
    }

    public void setSuccessfullySentStatus(boolean successfullySentStatus) {
        this.successfullySentStatus = successfullySentStatus;
    }

    @JsonIgnore
    public String getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(String multicast_id) {
        this.multicast_id = multicast_id;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isShouldNotify() {
        return shouldNotify;
    }

    public void setShouldNotify(boolean shouldNotify) {
        this.shouldNotify = shouldNotify;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
