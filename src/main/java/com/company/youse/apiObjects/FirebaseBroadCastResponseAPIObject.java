package com.company.youse.apiObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseBroadCastResponseAPIObject implements Serializable {
    private String message_id;

    public boolean isSuccess() {
        return !message_id.isEmpty();
    }


    public boolean isFailure() {
        return message_id.isEmpty();
    }
}
