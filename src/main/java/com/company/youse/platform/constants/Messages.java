package com.company.youse.platform.constants;

import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.contract.Type;

public final class Messages {

    private Messages() {
    }

    public static final InternalMessage SUCCESS = InternalMessage.builder()
            .type(Type.SUCCESS)
            .code("ok")
            .message("Completed Successfully")
            .build();

    public static final InternalMessage FAILED = InternalMessage.builder()
            .type(Type.FAILURE)
            .code("failure")
            .isTechnical(true)
            .message("Sorry we encountered an error")
            .build();

    public static final InternalMessage NOT_FOUND = InternalMessage.builder()
            .type(Type.WARNING)
            .code("not_found")
            .message("Sorry we could not find any thing for that")
            .httpStatus(404)
            .build();

    public static final InternalMessage NO_CONTENT = InternalMessage.builder()//
            .type(Type.INFO)//
            .code("no_content")//
            .message("We have no content for that")//
            .httpStatus(204)//
            .build();
}

