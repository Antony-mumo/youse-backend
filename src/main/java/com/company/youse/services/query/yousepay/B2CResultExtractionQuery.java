package com.company.youse.services.query.yousepay;

import com.company.youse.platform.services.Query;
import com.company.youse.services.command.yousepay.B2CResultCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class B2CResultExtractionQuery implements Query {
    private B2CResultCommand b2CResultCommand;
}

