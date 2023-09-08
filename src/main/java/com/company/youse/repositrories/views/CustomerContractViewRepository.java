package com.company.youse.repositrories.views;


import com.company.youse.models.views.CustomerContractView;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerContractViewRepository extends ReadOnlyRepository<CustomerContractView, Long> {

    List<CustomerContractView> findAllByCustomerId(Long customerId);

}
