package com.company.youse.repositrories.views;


import com.company.youse.models.views.ServiceProviderContractView;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderContractViewRepository extends ReadOnlyRepository<ServiceProviderContractView, Long> {

    List<ServiceProviderContractView> findAllByServiceProviderId(Long serviceProviderId);

}
