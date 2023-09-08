package com.company.youse.controller;

import com.company.youse.models.Job;
import com.company.youse.models.JobApplication;
import com.company.youse.models.User;
import com.company.youse.services.JobService;
import com.company.youse.services.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@Transactional
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private final Util util;

    @RequestMapping(value = "/api/jobs/post", method = RequestMethod.POST)
    public ResponseEntity<?> postJob(HttpServletRequest request, @RequestBody Job job) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.postJob(job, user);
    }


    @RequestMapping(value = "/api/jobs/all", method = RequestMethod.GET)
    public ResponseEntity<?> getJobs(HttpServletRequest request) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.getAllJobs(user);
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/jobs/available", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableJobs(HttpServletRequest request) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.getAvailableJobs(user);
    }

    /**
     * Endpoint to enable service provider get all the jobs they have applied to
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/jobs/applied", method = RequestMethod.GET)
    public ResponseEntity<?> getAppliedJobs(HttpServletRequest request) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.getAppliedJobs(user);
    }

    /**
     * Endpoint to enable a customer to get all the jobs they have posted
     * @param request
     * @return response with a list of jobs posted by customer as body
     */
    @RequestMapping(value = "/api/jobs/posted", method = RequestMethod.GET)
    public ResponseEntity<?> getPostedJobs(HttpServletRequest request) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.getPostedJobs(user);
    }


    /**
     * Endpoint for a customer to get all job application to a job they posted
     * @param request
     * @param jobId is the id to the specific job in which customer needs to fetch applications for
     * @return  response with a list of jobApplications for job as body
     */
    @RequestMapping(value = "/api/jobs/{jobId}/applications", method = RequestMethod.GET)
    public ResponseEntity<?> getJobApplications(HttpServletRequest request, @PathVariable(value="jobId") Long jobId) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.getJobApplications(user, jobId);
    }


    /**
     * Endpoint for service provider to apply for a job, receives a jobApplication object from service provider
     * @param request
     * @param jobApplication
     * @return
     */
    @RequestMapping(value = "/api/jobs/apply", method = RequestMethod.POST)
    public ResponseEntity<?> applyJob(HttpServletRequest request, @RequestBody JobApplication jobApplication) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.applyJob(jobApplication, user);
    }


    /**
     * endpoint to check if a service provider has applied to a job
     * @param request
     * @param jobId
     * @return
     */
    @RequestMapping(value = "/api/jobs/{jobId}/check-applied", method = RequestMethod.GET)
    public ResponseEntity<?> checkIfJobApplied(HttpServletRequest request, @PathVariable(value="jobId") Long jobId) {
        User serviceProvider = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.checkApplied(jobId, serviceProvider);
    }
}
