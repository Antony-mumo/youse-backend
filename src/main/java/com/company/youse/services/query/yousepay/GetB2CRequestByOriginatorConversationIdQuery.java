package com.company.youse.services.query.yousepay;

import com.company.youse.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetB2CRequestByOriginatorConversationIdQuery implements Query {
    protected String conversationId;
}

