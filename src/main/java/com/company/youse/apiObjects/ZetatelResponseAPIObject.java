package com.company.youse.apiObjects;


import lombok.Data;

import java.io.Serializable;

@Data
public class ZetatelResponseAPIObject implements Serializable {
    public String status;
    public String mobile;
    public String invalidMobile;
    public String transactionId;
    public String statusCode;
    public String reason;
    public String msgId;
}