package com.github.salilvnair.ccf.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "ccf.core")
@Getter
@Setter
public class CcfCoreEntityConfig {
    private Map<String, String> tables = new HashMap<>();
}
