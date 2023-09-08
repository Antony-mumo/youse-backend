package com.company.youse.apiObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseResponseAPIObject implements Serializable {
    private String multicast_id;
    private Integer success;
    private Integer failure;
    private  Integer canonical_ids;

    public boolean isSuccess() {
        return success == 1 || failure == 0;
    }


    public boolean isFailure() {
        return failure == 1 || success==1;
    }
}
