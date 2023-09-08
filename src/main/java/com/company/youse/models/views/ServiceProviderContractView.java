package com.company.youse.models.views;

import com.company.youse.enums.ContractStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Class contains a contract object to a service provider
 *
 */
@Entity
//@Immutable
@Getter
//using subselect statement to prevent generating table in database
@Subselect("SELECT " +
        "c.jobApplication_id as id, " +
        "s.user_id as serviceProviderId, " +
        "j.jobTitle as jobTitle, " +
        "c.amountClientHasToPay, " +
        "c.createdDateTime as createdDateTime, " +
        "c.feedbackToCustomer, " +
        "c.feedbackToServiceProvider, " +
        "c.ratingToCustomer, " +
        "c.ratingToServiceProvider, " +
        "c.status, " +
        "j.description, " +
        "ja.addedInformation as messageFromSP, " +
        "j.location " +
        "FROM serviceProvider s " +
        "left join jobApplication ja on ja.serviceProvider_id = s.user_id " +
        "right join job j on j.id = ja.job_id  " +
        "right join contract c on c.jobApplication_id = ja.id  " +
        "")
public class ServiceProviderContractView {

    @Id
    Long id;
    Long serviceProviderId;
    String jobTitle;
    BigDecimal amountClientHasToPay;
    LocalDateTime createdDateTime;
    String feedbackToCustomer;
    String feedbackToServiceProvider;
    Integer ratingToCustomer;
    Integer ratingToServiceProvider;
    ContractStatusEnum status;
    String description;
    String messageFromSP;
    String location;

    @JsonIgnore
    public String getFeedbackToCustomer() {
        return feedbackToCustomer;
    }

    @JsonIgnore
    public Integer getRatingToCustomer() {
        return ratingToCustomer;
    }

    public boolean isHasGivenFeedback() {
        return feedbackToCustomer != null;
    }

    public boolean isHasGivenRating() {
        return ratingToCustomer != null;
    }

}
