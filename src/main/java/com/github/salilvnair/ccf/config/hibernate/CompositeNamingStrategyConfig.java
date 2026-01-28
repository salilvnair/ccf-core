package com.github.salilvnair.ccf.config.hibernate;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class CompositeNamingStrategyConfig
        implements HibernatePropertiesCustomizer {

    private final ApplicationContext ctx;

    public CompositeNamingStrategyConfig(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void customize(Map<String, Object> props) {

        List<PhysicalNamingStrategy> discovered =
                ctx.getBeansOfType(PhysicalNamingStrategy.class)
                        .values()
                        .stream()
                        .filter(s -> !(s instanceof CcfPhysicalNamingStrategy))
                        .toList();

        props.put(
                "hibernate.physical_naming_strategy",
                new CcfPhysicalNamingStrategy(discovered)
        );
    }
}
