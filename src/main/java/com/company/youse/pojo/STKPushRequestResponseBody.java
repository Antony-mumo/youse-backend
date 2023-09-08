package com.company.youse.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class STKPushRequestResponseBody {
    private boolean isAccepted;
    private String description;
    private String merchantRequestId;
    private String checkoutRequestId;
    private String customerMessage;
}
