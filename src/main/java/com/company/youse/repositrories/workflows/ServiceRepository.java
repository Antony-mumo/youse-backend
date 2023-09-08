package com.company.youse.repositrories.workflows;

/*
  @author: Austin Oyugi
  @since: 6/4/22
  mail: austinoyugi@gmail.com
*/

import com.company.youse.models.workflows.Service;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository  {
    boolean existsById(String s);
    Optional<Service> findById(String s);
    Iterable<Service> findAll();
}
