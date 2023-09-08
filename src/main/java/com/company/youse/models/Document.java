package com.company.youse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "document")
public class Document {
    public Document(){}

    // note use for creating a new object only
    public Document( ServiceProvider serviceProvider , String documentName, String documentType, byte[] bytes) {
        this.documentName = documentName;
        this.documentType = documentType;
        this.bytes = bytes;
        this.serviceProvider = serviceProvider;
        this.isVerified = false;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @NotNull
    private String documentName;

    @NotNull
    private String documentType;

    @NotNull
    @Column(columnDefinition="tinyint(0) default false")
    private Boolean isVerified;

    @Column(name = "bytes", length = 1000)
    private byte[] bytes;

    @ManyToOne
    @JoinColumn(name="serviceProvider_id", nullable=false)
    private ServiceProvider serviceProvider;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @JsonIgnore
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @JsonIgnore
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", documentName='" + documentName + '\'' +
                ", documentType='" + documentType + '\'' +
                '}';
    }
}
