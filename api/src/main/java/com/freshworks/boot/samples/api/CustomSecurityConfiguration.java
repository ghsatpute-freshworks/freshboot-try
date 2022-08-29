package com.freshworks.boot.samples.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class CustomSecurityConfiguration
        implements com.freshworks.starter.web.security.SecurityConfiguration {
    @Override
    public Map<HttpMethod, String[]> unauthenticatedPathPatterns() {
        //***Important note:*** If a path has to be excluded from both user authentication and client authentication,
        //exact same HttpMethod and path pattern needs to be listed in
        //{@code unauthenticatedClientCredentialsPathPatterns} too.
        return Map.of(HttpMethod.GET, new String[]{"/api/v1/about"});
    }

    @Override
    public Map<HttpMethod, String[]> unauthenticatedClientCredentialsPathPatterns() {
        final Map<HttpMethod, String[]> map = new HashMap<>();
        map.put(null, new String[]{"/**"});
        //***Important note:*** If a path has to be excluded from both user authentication and client authentication,
        //exact same HttpMethod and path pattern needs to be listed in {@code unauthenticatedPathPatterns} too.
        map.put(HttpMethod.GET, new String[]{"/api/v1/about"});
        return map;
    }

    @Override
    public List<String> clientSources() {
        return List.of("freshchat");
    }
}
