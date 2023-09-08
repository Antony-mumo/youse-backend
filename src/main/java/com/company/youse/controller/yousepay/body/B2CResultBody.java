package com.company.youse.controller.yousepay.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class B2CResultBody {

    @JsonProperty("Result")
    private Map<String, Object> result;
}
