package com.freshworks.boot.samples.api;

import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.repository.TodoRepository;
import com.freshworks.starter.web.security.PermissionsDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.freshworks.boot.samples"})
@EntityScan(basePackageClasses = Todo.class)
@EnableJpaRepositories(basePackageClasses = TodoRepository.class)
public class ApiApplication {
    @Bean
    public PermissionsDecoder permissionsDecoder() {
        return new PermissionsDecoder(getClass().getResourceAsStream("/permissions.properties"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
