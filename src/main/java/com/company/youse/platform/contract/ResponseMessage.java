package com.company.youse.platform.contract;


import com.company.youse.platform.InternalMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 8964325911666373739L;

    private Type type;

    private String code;

    private String message;

    private boolean isTechnical;

    public InternalMessage to() {
        return InternalMessage.builder()
                .type(type)
                .code(code)
                .message(message)
                .isTechnical(isTechnical)
                .build();
    }

}