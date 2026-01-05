package com.github.salilvnair.ccf.config.hibernate;

import com.github.salilvnair.ccf.config.CcfCoreEntityConfig;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.*;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CcfCorePhysicalNamingStrategy extends PhysicalNamingStrategySnakeCaseImpl implements PhysicalNamingStrategy {

    private final CcfCoreEntityConfig config;

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {

        if(identifier == null) {
            return null;
        }
        String text = identifier.getText();
        if(text.startsWith("CCF_")) {
            String entityNameKey = text.substring(4);
            Map<String, String> tables = config.getTables();
            if(tables.containsKey(entityNameKey)) {
                String dynamicTableName = tables.get(entityNameKey);
                return Identifier.toIdentifier(dynamicTableName, identifier.isQuoted());
            }
        }
        return identifier;
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalCatalogName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalSequenceName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalColumnName(identifier, jdbcEnvironment);
    }
}
