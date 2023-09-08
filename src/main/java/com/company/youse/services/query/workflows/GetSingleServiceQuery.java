package com.company.youse.services.query.workflows;

import com.company.youse.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
  @author: Austin Oyugi
  @since: 6/4/22
  mail: austinoyugi@gmail.com
*/

@Data
@AllArgsConstructor
public class GetSingleServiceQuery implements Query {
    private String serviceId;

    @Override
    public String identifier() {
        return serviceId;
    }
}
