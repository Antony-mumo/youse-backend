package com.company.youse.controller;

import com.company.youse.models.ServiceType;
import com.company.youse.models.User;
import com.company.youse.services.ServiceProviderService;
import com.company.youse.services.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Transactional
@RequiredArgsConstructor
public class SkillsController {

      private final Util util;

      private final ServiceProviderService serviceProviderService;


    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/skills/set", method = RequestMethod.GET)
    public ResponseEntity<?> setServiceSkill(HttpServletRequest request, @RequestParam String skill ) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return serviceProviderService.setServiceProviderSkill(user,skill);
    }


    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/skills/remove", method = RequestMethod.GET)
    public ResponseEntity<?> removeServiceSkill(HttpServletRequest request, @RequestParam String skill ) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return serviceProviderService.removeServiceProviderSkill(user,skill);
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/skills/all", method = RequestMethod.GET)
    public ResponseEntity<?> fetchAllServiceSkills(HttpServletRequest request ) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return serviceProviderService.getServiceProviderSkills(user);
    }


    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/skills/update", method = RequestMethod.POST)
    public ResponseEntity<?> setServiceSkill(HttpServletRequest request, @RequestBody List<ServiceType> serviceTypeList) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return serviceProviderService.updateServiceProviderSkills(user,serviceTypeList);
    }

}
