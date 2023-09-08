package com.company.youse.controller;

import com.company.youse.models.ServiceType;
import com.company.youse.services.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Transactional
@RequiredArgsConstructor
public class AdminController {


    private final ServiceTypeService serviceTypeService;

    @RequestMapping(value = "/api/service/types/add", method = RequestMethod.POST)
    public ResponseEntity<?> addServiceType(HttpServletRequest request, @RequestBody ServiceType serviceType) throws IOException {
        return serviceTypeService.addServiceType(serviceType);
    }

}
