package com.freshworks.boot.samples.api.v1.controller;

import com.freshworks.boot.test.api.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ComponentScan(basePackages = {"com.freshworks.boot.samples"})
@EntityScan(basePackages = {"com.freshworks.boot.samples"})
@ActiveProfiles("test")
@Transactional
class AboutControllerTest {
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_USER_ID = "user_id";

    private static final String JWT;

    static {
        try {
            JWT = JwtUtil.generateJwtString(Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception occurred", e);
        }
    }

    private static final String JWT_INVALID = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImR1bW15In0.eyJhY2NvdW50X2lkIjoiMSIsInVzZXJfaWQiOiIxIiwicHJvZHVjdCI6ImZyZXNoZGVzayIsIm9yZ19pZCI6IjEiLCJhY2NvdW50X2RvbWFpbiI6ImRvbWFpbiIsInByaXZpbGVnZXMiOiIwIn0.nGCjKEhLLxezXVPDnnhmbdxsITgE4kmJfhhBJxeyR4NmEqWlv0dS_E3ezvLeinHhE0EV8IPwemEkxGI456mGqcyvnnH-PC62Tlgt6-iq-qWdskIyKSVBvOKXjtxPC7x94rATrL8vwPWqGuGVK1eLD2mKhcs0CvxM3HWMlwesVxQTEw-zFO1XLK-UJEsxicvIdYioht_z12dR3CYYNCtpIF1YiB9cuU0jM_UZYWZbvXMhKvPghxYeb3aLRYo-aBqJoRfvw4DS-wZKrlrvF5JjCR-edF5Oe-vlLlnaHAgyaeg5dxR84jI2YbQRfKblJAlfWERnZV_Ff-H85G8eyORXhQ";
    private static final String HOST = "host";

    @Autowired
    private MockMvc mvc;

    private final HttpHeaders httpHeaders = getHttpHeaders();

    @Test
    public void about_whenCalled_thenReturnsResult() throws Exception {
        mvc
                .perform(
                        get("/api/v1/about")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{'name': 'To-do service'}"));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + JWT);
        httpHeaders.add(HOST, JwtUtil.ACCOUNT_DOMAIN);
        return httpHeaders;
    }

}