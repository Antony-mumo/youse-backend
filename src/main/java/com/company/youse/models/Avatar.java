package com.company.youse.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "avatar")
public class Avatar {
    public Avatar(){}

    public Avatar(String name, String type, byte[] picByte, byte[] miniBytes) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
        this.miniByte = miniBytes;
    }



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @Column(name = "picByte", length = 1000)
    private byte[] picByte;


    @Column(name = "miniByte", length = 1000)
    private byte[] miniByte;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getMiniByte() {
        return miniByte;
    }

    public void setMiniByte(byte[] miniByte) {
        this.miniByte = miniByte;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
