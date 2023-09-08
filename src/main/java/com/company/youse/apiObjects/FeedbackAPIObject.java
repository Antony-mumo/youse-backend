package com.company.youse.apiObjects;

import lombok.Data;

public @Data class FeedbackAPIObject {

    private Long contractId;

    private String feedbackToServiceProvider;

    private String feedbackToCustomer;

    private String serviceProviderFeedbackToYouse;

    private String customerFeedbackToYouse;

    private Integer ratingToServiceProvider;

    private Integer ratingToCustomer;

    private Integer serviceProviderRatingToYouse;

    private Integer customerRatingToYouse;
}
