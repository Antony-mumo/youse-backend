package com.company.youse.models.views;

import com.company.youse.enums.ContractStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Class contains a contract object to a customer
 *
 */
@Getter
@Entity
//@Immutable
//using subselect statement to prevent generating table in database
@Subselect("SELECT " +
        "c.jobApplication_id as id, " +
        "s.user_id as customerId, " +
        "j.jobTitle as jobTitle, " +
        "c.amountClientHasToPay, " +
        "c.createdDateTime, " +
        "c.feedbackToCustomer, " +
        "c.feedbackToServiceProvider, " +
        "c.ratingToCustomer, " +
        "c.ratingToServiceProvider, " +
        "c.status, " +
        "j.description, " +
        "j.location, " +
        "ja.addedInformation as messageFromSP " +
        "FROM customer s " +
        "right join job j on j.customer_id = s.user_id  " +
        "right join jobApplication ja on j.id = ja.job_id " +
        "right join contract c on c.jobApplication_id = ja.id  " +
        "")
public class CustomerContractView {
    @Id
    Integer id;
    Integer customerId;
    String jobTitle;
    Float amountClientHasToPay;
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
    public String getFeedbackToServiceProvider() {
        return feedbackToServiceProvider;
    }

    @JsonIgnore
    public Integer getRatingToServiceProvider() {
        return ratingToServiceProvider;
    }

    public boolean isHasGivenFeedback() {
        return feedbackToServiceProvider != null;
    }

    public boolean isHasGivenRating() {
        return ratingToServiceProvider != null;
    }
}
