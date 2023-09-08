package com.company.youse.services;

import com.company.youse.repositrories.ServiceTypeRepository;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.models.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ResponseEntity<?> addServiceType(ServiceType serviceType) {
        if(serviceTypeRepository.findTopByType(serviceType.getType())==null){
            if(serviceType.getType()!=null){
                serviceTypeRepository.save(serviceType);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            throw new BadRequestException("service type can not be null");
        }else{
            throw new BadRequestException("Service type "+ serviceType.getType()+ " already exists");
        }
    }
}
