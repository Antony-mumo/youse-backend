package com.company.youse.services.query.workflows;

import com.company.youse.models.workflows.Service;
import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.repositrories.workflows.ServiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/*
  @author: Austin Oyugi
  @since: 6/4/22
  mail: austinoyugi@gmail.com
*/

@org.springframework.stereotype.Service
public class GetAllServicesQueryService extends QueryBaseService<GetSingleServiceQuery, List<Service>> {

    private final ServiceRepository serviceRepository;

    public GetAllServicesQueryService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public QueryResult<List<Service>> execute(GetSingleServiceQuery query) {

        if (Optional.ofNullable(query.getServiceId()).isPresent()){
            var service = serviceRepository.findById(query.getServiceId());
            return service.map(value -> new QueryResult.Builder<List<Service>>()
                    .data(List.of(value)).ok().build()).orElseGet(() ->
                    new QueryResult.Builder<List<Service>>().ok().notFound()
                    .build());
        }

        return new QueryResult.Builder<List<Service>>().data(
                StreamSupport.stream(serviceRepository.findAll().spliterator(),false)
                        .collect(Collectors.toList())).ok().build();
    }
}
