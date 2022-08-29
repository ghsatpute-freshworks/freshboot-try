package com.freshworks.boot.samples.api.v1.controller;

import com.freshworks.boot.samples.api.v1.dto.AboutDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample controller for demonstrating unauthenticated endpoint.
 * Path under `/api/v1/about` is unauthenticated as configured in CustomSecurityConfiguration
 */
@RestController()
@RequestMapping(path = "/api/v1/about")
public class AboutController {
    @GetMapping
    public AboutDto about() {
        return new AboutDto().name("To-do service");
    }
}
