package com.company.youse.models.notificationData;

import com.company.youse.apiObjects.ServiceProviderProfileApiObject;
import com.company.youse.models.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kely
 * on 25, June 2022
 * http://stackoverflow.com/u/14795945
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableServiceProvider {
    // service provider is contained in job application
    JobApplication jobApplication;
}
