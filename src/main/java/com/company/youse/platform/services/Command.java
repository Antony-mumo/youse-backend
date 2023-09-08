package com.company.youse.platform.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.util.Strings;

public interface Command {

    @JsonIgnore
    default String identifier() {
        return Strings.EMPTY;
    }
}
