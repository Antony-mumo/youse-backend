package com.company.youse.controller;

import com.company.youse.config.PropertiesFetcher;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.models.User;
import com.company.youse.services.ServiceProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Transactional
@RequiredArgsConstructor
public class ServiceProviderController {

    private final PropertiesFetcher properties;

    private final ServiceProviderService serviceProviderService;

    @RequestMapping(value = "/api/service-provider/signup", method = RequestMethod.POST /*, consumes = MediaType.MULTIPART_FORM_DATA_VALUE*/)
    public ResponseEntity<?> signup(HttpServletRequest request, @RequestBody User user) throws IOException {
        System.out.println("signing up user new");
        System.out.println(user);
        if(properties.getCustomAuthToken().equals(request.getHeader("Authorization"))) {
            return serviceProviderService.createServiceProvider(user);
        }
        else
            throw new BadRequestException("Request not authenticated");
    }

}
