package com.company.youse.controller.res.job;

import com.company.youse.models.Job;
import com.company.youse.models.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Kely
 * on 21, June 2022
 * http://stackoverflow.com/u/14795945
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {

    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long noOfWorkersNeeded;
    private ServiceType serviceType;
    private String location;
    private String description;
    private BigDecimal pricedAmount;

    public static JobResponse copy(Job job){
        return new JobResponse(
                job.getId(),
                job.getLattitude(),
                job.getLongitude(),
                job.getNoOfWorkersNeeded(),
                job.getServiceType(),
                job.getLocation(),
                job.getDescription(),
                job.getPricedAmount()
        );
    }
}
