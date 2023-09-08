package com.company.youse.controller.workflows;

import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.ResponseConverter;
import com.company.youse.services.query.workflows.GetAllServicesQueryService;
import com.company.youse.services.query.workflows.GetSingleServiceQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
  @author: Austin Oyugi
  @since: 6/4/22
  mail: austinoyugi@gmail.com
*/

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final GetAllServicesQueryService getAllServicesQueryService;

    public ServiceController(GetAllServicesQueryService getAllServicesQueryService) {
        this.getAllServicesQueryService = getAllServicesQueryService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllJobs(@RequestParam(required = false) String serviceId){
        return new ResponseConverter().convert(getAllServicesQueryService
                .decorate(new GetSingleServiceQuery(serviceId)));
    }
}
