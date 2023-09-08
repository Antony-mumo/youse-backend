package com.company.youse.models.workflows;

/*
  @author: Austin Oyugi
  @since: 6/4/22
  mail: austinoyugi@gmail.com
*/

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Service {
    private String id;
    private String name;
    private String description;
    private Boolean isGraduated = true;
    private List<Weight> weights;

    @Data
    private static class Weight {
        private String id;
        private String quantity;
        private BigDecimal price;
    }
}
