package com.company.youse.controller;

import com.company.youse.apiObjects.FeedbackAPIObject;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.User;
import com.company.youse.services.JobService;
import com.company.youse.services.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@Transactional
@RequiredArgsConstructor
public class ContractController {


    private final JobService jobService;

    private final Util util;


    @RequestMapping(value = "/api/contracts/{jobApplicationId}/start-contract", method = RequestMethod.GET)
    public ResponseEntity<?> hireServiceProvider(HttpServletRequest request, @PathVariable(value="jobApplicationId") Long jobApplicationId) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.hireServiceProvider(jobApplicationId, user);
    }

    @RequestMapping(value = "/api/contracts/{contractId}/end-contract", method = RequestMethod.GET)
    public ResponseEntity<?> endContract(HttpServletRequest request, @PathVariable(value="contractId") Long contractId) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.endContract(contractId, user);
    }


    @RequestMapping(value = "/api/contracts/feedback", method = RequestMethod.POST)
    public ResponseEntity<?> postFeedback(HttpServletRequest request, @RequestBody FeedbackAPIObject feedback) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.addFeedback(feedback, user);
    }


    @RequestMapping(value = "/api/contracts/{contractId}/request-pay", method = RequestMethod.GET)
    public ResponseEntity<?> submitContractForApproval(HttpServletRequest request, @PathVariable(value="contractId") Long contractId) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return jobService.submitContractForApproval(contractId, user);
    }


    @RequestMapping(value = "/api/contracts/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> getServiceProviderContracts(HttpServletRequest request, @PathVariable(value="role") String role, @RequestParam(value="status") String status) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        if (user != null)
            return jobService.getContracts(user, role, status);
        else
            throw new MissingException("user not found");
    }

}
