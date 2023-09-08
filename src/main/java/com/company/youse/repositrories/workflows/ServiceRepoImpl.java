package com.company.youse.repositrories.workflows;

import com.company.youse.models.workflows.Service;
import com.company.youse.utils.AppUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/*
  @author: Austin Oyugi
  @since: 6/4/22
  mail: austinoyugi@gmail.com
*/

@Component
public class ServiceRepoImpl implements ServiceRepository{

    @Override
    public Optional<Service> findById(String s) {
        return getAsList(findAll()).stream().filter(service -> service.getId().equals(s))
                .findFirst();
    }

    @Override
    public boolean existsById(String s) {
        return getAsList(findAll()).stream().anyMatch(service -> service.getId().equals(s));
    }

    @Override
    @SneakyThrows
    public Iterable<Service> findAll() {
         var servicesJSON = AppUtils.FileUtils.getResourceString("jobs.json");
         var services = new ObjectMapper().readValue(servicesJSON, Service[].class);
         return Arrays.asList(services);
    }

    private List<Service> getAsList(Iterable<Service> serviceIterable){
        return StreamSupport.stream(serviceIterable.spliterator(), false).collect(Collectors.toList());
    }
}
