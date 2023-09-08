package com.company.youse.controller.yousepay.body;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class STKPushResultBody {
    //Body
        //stkCallBack
            //data
                //callbackMetadata
                    //List Item
    @JsonProperty("Body")
    private Map<String, Object > body = new LinkedHashMap<>();
}
