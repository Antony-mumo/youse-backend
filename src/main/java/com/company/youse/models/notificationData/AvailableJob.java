package com.company.youse.models.notificationData;

import com.company.youse.controller.res.job.JobResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kely
 * on 25, June 2022
 * http://stackoverflow.com/u/14795945
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableJob {
    JobResponse job;
}
