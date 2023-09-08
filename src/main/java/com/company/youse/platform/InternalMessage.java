package com.company.youse.platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.company.youse.platform.contract.ResponseMessage;
import com.company.youse.platform.contract.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InternalMessage implements Serializable {

    private static final long serialVersionUID = 4132925281439221077L;

    @Builder.Default
    private Type type = Type.FAILURE;

    @EqualsAndHashCode.Include
    private String code;

    private String message;

    @JsonIgnore
    protected int httpStatus;

    @Builder.Default
    @JsonIgnore
    private boolean isTechnical = false;

    @EqualsAndHashCode.Include
    private String[] arguments;

    public ResponseMessage toMessage() {
        return ResponseMessage.builder()//
                .code(code)//
                .type(type)//
                .message(message)
                .isTechnical(isTechnical)//
                .build();
    }
}

