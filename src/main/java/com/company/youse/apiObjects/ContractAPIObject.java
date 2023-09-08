package com.company.youse.apiObjects;

import com.company.youse.models.Contract;
import com.company.youse.models.Job;
import lombok.Data;

@Data
public class ContractAPIObject {
    Contract contract;
    Job job;
}
