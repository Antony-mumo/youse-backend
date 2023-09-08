package com.company.youse.repositrories;

import com.company.youse.models.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {


    ServiceType findTopByType(String serviceType);

}
