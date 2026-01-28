package com.github.salilvnair.ccf.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@AutoConfigurationPackage(basePackages = "com.github.salilvnair.ccf")
@ComponentScan(basePackages = "com.github.salilvnair.ccf")
@EntityScan(basePackages = "com.github.salilvnair.ccf.core.entity")
@EnableJpaRepositories(basePackages = "com.github.salilvnair.ccf.core.repository")
public class CcfCoreAutoConfiguration {
}