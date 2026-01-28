package com.github.salilvnair.ccf.config.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.List;

public class CcfPhysicalNamingStrategy implements PhysicalNamingStrategy, ApplicationContextAware {

    private static List<PhysicalNamingStrategy> delegates;

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        delegates = ctx.getBeansOfType(PhysicalNamingStrategy.class)
                        .values()
                        .stream()
                        .filter(s -> !(s instanceof CcfPhysicalNamingStrategy))
                        .toList();
    }

    @Override
    public Identifier toPhysicalTableName(Identifier id, JdbcEnvironment env) {
        if (id == null) return null;
        for (PhysicalNamingStrategy strategy : delegates) {
            id = strategy.toPhysicalTableName(id, env);
        }
        return id;
    }

    @Override public Identifier toPhysicalCatalogName(Identifier i, JdbcEnvironment e) { return null; }
    @Override public Identifier toPhysicalSchemaName(Identifier i, JdbcEnvironment e) { return i; }
    @Override public Identifier toPhysicalSequenceName(Identifier i, JdbcEnvironment e) { return i; }
    @Override public Identifier toPhysicalColumnName(Identifier i, JdbcEnvironment e) { return i; }
}
