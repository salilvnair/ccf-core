package com.github.salilvnair.ccf.config.hibernate;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import java.util.List;

@RequiredArgsConstructor
public class CcfPhysicalNamingStrategy implements PhysicalNamingStrategy {

    private final List<PhysicalNamingStrategy> delegates;


    @Override
    public Identifier toPhysicalTableName(Identifier id, JdbcEnvironment env) {
        if (id == null) return null;
        for (PhysicalNamingStrategy strategy : delegates) {
            Identifier next = strategy.toPhysicalTableName(id, env);
            if (!next.equals(id)) {
                return next;
            }
        }
        return id;
    }

    @Override public Identifier toPhysicalCatalogName(Identifier i, JdbcEnvironment e) { return null; }
    @Override public Identifier toPhysicalSchemaName(Identifier i, JdbcEnvironment e) { return i; }
    @Override public Identifier toPhysicalSequenceName(Identifier i, JdbcEnvironment e) { return i; }
    @Override public Identifier toPhysicalColumnName(Identifier i, JdbcEnvironment e) { return i; }
}
