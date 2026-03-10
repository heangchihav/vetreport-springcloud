package com.platform.regionservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.platform.regionservice.repository")
@EntityScan(basePackages = "com.platform.regionservice.model")
@EnableTransactionManagement
public class JpaConfig {
}
