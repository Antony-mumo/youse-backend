package com.company.youse.controller;
/* Dev Kelyn created the file on 2021-06-06 inside the package - com.company.youse.config.security */

import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
public class MainRestController {
    @GetMapping("/api/test")
    public String index() {
        return "{\"message\" : \"Greetings! Glad to inform you your test was successful\"}";
    }
}
