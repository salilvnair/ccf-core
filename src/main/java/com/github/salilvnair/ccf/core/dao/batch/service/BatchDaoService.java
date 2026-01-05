package com.github.salilvnair.ccf.core.dao.batch.service;

import com.github.salilvnair.ccf.core.dao.batch.reflect.BatchJoinColumn;
import com.github.salilvnair.ccf.util.commonutil.lang.AnnotationUtil;
import com.github.salilvnair.ccf.util.commonutil.lang.ReflectionUtil;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Set;

@Service
public class BatchDaoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void batchInsert(List<?> entityList) {
        String sql = generateInsertQuery(entityList);

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object entity = entityList.get(i);
                BatchDaoService.addInsertPreparedStatementValues(entity, ps, i);
            }

            @Override
            public int getBatchSize() {
                return entityList.size();
            }
        });
    }

    @Transactional
    public void batchUpdate(List<?> entityList) {
        String sql = generateUpdateQuery(entityList);

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object entity = entityList.get(i);
                BatchDaoService.addUpdatePreparedStatementValues(entity, ps, i);
            }

            @Override
            public int getBatchSize() {
                return entityList.size();
            }
        });
    }


    public String generateInsertQuery(List<?> entityList) {
        Object entity = entityList.get(0);
        if(entity.getClass().isAnnotationPresent(Table.class)) {
            Table table = entity.getClass().getAnnotation(Table.class);
            String tableName = table.name();
            return generateInsertQuery(tableName, entityList);
        }
        return null;
    }

    public String generateUpdateQuery(List<?> entityList) {
        Object entity = entityList.get(0);
        if(entity.getClass().isAnnotationPresent(Table.class)) {
            Table table = entity.getClass().getAnnotation(Table.class);
            String tableName = table.name();
            return generateUpdateQuery(tableName, entityList);
        }
        return null;
    }

    private String generateInsertQuery(String tableName, List<?> dataList) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tableName).append(" (");
        Object entity = dataList.get(0);
        Set<Field> columnFields = AnnotationUtil.extractAnnotatedFields(entity.getClass(), Column.class);
        Set<Field> joinColumnFields = AnnotationUtil.extractAnnotatedFields(entity.getClass(), JoinColumn.class);
        List<Field> eligibleColumnFields = columnFields.stream().filter(columnField -> !columnField.isAnnotationPresent(Id.class)).toList();
        Set<Field> idFields = AnnotationUtil.extractAnnotatedFields(entity.getClass(), Id.class);
        for (Field idField : idFields) {
            Column idColumn = idField.getAnnotation(Column.class);
            query.append(idColumn.name()).append(", ");
        }
        for(Field field : eligibleColumnFields) {
            Column column = field.getAnnotation(Column.class);
            query.append(column.name()).append(", ");
        }
        for(Field field : joinColumnFields) {
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            query.append(joinColumn.name()).append(", ");
        }
        query.deleteCharAt(query.length() - 1);
        query.deleteCharAt(query.length() - 1);
        query.append(") VALUES ");
        query.append("(");
        for (Field idField : idFields) {
            if(idField.isAnnotationPresent(SequenceGenerator.class)) {
                SequenceGenerator sequenceGenerator = idField.getAnnotation(SequenceGenerator.class);
                query.append(sequenceGenerator.sequenceName()).append(".nextval, ");
            }
        }
        for(Field field : eligibleColumnFields) {
            query.append("?, ");
        }
        for(Field field : joinColumnFields) {
            query.append("?, ");
        }
        query.deleteCharAt(query.length() - 1);
        query.deleteCharAt(query.length() - 1);
        query.append(")");
        return query.toString();
    }

    private String generateUpdateQuery(String tableName, List<?> dataList) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(tableName).append(" SET ");
        Object entity = dataList.get(0);

        // Extract fields annotated with @Column
        Set<Field> columnFields = AnnotationUtil.extractAnnotatedFields(entity.getClass(), Column.class);
        List<Field> eligibleColumnFields = columnFields.stream().filter(columnField -> !columnField.isAnnotationPresent(Id.class)).toList();

        // Append column names and placeholders for update
        for (Field field : eligibleColumnFields) {
            Column column = field.getAnnotation(Column.class);
            query.append(column.name()).append(" = ?, ");
        }
        query.deleteCharAt(query.length() - 1);
        query.deleteCharAt(query.length() - 1);

        // Extract the field annotated with @Id for the WHERE clause
        Field idField = columnFields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No @Id field found in the entity class"));
        Column idColumn = idField.getAnnotation(Column.class);
        query.append(" WHERE ").append(idColumn.name()).append(" = ?");

        return query.toString();
    }

    private static void addUpdatePreparedStatementValues(Object entity, PreparedStatement ps, int i) throws SQLException {
        Set<Field> columnFields = AnnotationUtil.extractAnnotatedFields(entity.getClass(), Column.class);
        List<Field> eligibleColumnFields = columnFields.stream().filter(columnField -> !columnField.isAnnotationPresent(Id.class)).toList();

        // Set values for non-Id fields
        for (int index = 0; index < eligibleColumnFields.size(); index++) {
            int parameterIndex = index + 1;
            Field field = eligibleColumnFields.get(index);
            Object fieldValue = ReflectionUtil.findFieldValue(entity, field);
            addPreparedStatementValue(ps, parameterIndex, fieldValue, field.getType());
        }

        // Set value for the Id field (for WHERE clause)
        Field idField = columnFields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No @Id field found in the entity class"));
        Object idValue = ReflectionUtil.findFieldValue(entity, idField);
        addPreparedStatementValue(ps, eligibleColumnFields.size() + 1, idValue, idField.getType());
    }


    private static void addInsertPreparedStatementValues(Object entity, PreparedStatement ps, int i) throws SQLException {
        Set<Field> columnFields = AnnotationUtil.extractAnnotatedFields(entity.getClass(), Column.class);
        List<Field> eligibleColumnFields = columnFields.stream().filter(columnField -> !columnField.isAnnotationPresent(Id.class)).toList();
        Set<Method> methods = AnnotationUtil.extractAnnotatedPublicMethods(entity.getClass(), BatchJoinColumn.class);
        for (int index = 0; index < eligibleColumnFields.size(); index++) {
            int parameterIndex = index + 1;
            Field field = eligibleColumnFields.get(index);
            Object fieldValue = ReflectionUtil.findFieldValue(entity, field);
            addPreparedStatementValue(ps, parameterIndex, fieldValue, field.getType());
        }
        for (int index = 0; index < methods.size(); index++) {
            int parameterIndex = eligibleColumnFields.size() + index + 1;
            Method method = methods.stream().toList().get(index);
            Object fieldValue = ReflectionUtil.invokeMethod(entity, method);
            addPreparedStatementValue(ps, parameterIndex, fieldValue, method.getReturnType());
        }
    }

    private static void addPreparedStatementValue(PreparedStatement ps, int parameterIndex, Object fieldValue, Class<?> type) throws SQLException {
        if(type == String.class) {
            if(fieldValue != null) {
                ps.setString(parameterIndex, (String) fieldValue);
            }
            else {
                ps.setNull(parameterIndex, Types.VARCHAR);
            }
        }
        else if(type == Integer.class) {
            if (fieldValue != null) {
                ps.setInt(parameterIndex, (Integer) fieldValue);
            }
            else {
                ps.setNull(parameterIndex, Types.INTEGER);
            }
        }
        else if(type == Long.class) {
            if (fieldValue != null) {
                ps.setLong(parameterIndex, (Long) fieldValue);
            }
            else {
                ps.setNull(parameterIndex, Types.BIGINT);
            }
        }
        else if(type == Double.class) {
            if (fieldValue != null) {
                ps.setDouble(parameterIndex, (Double) fieldValue);
            }
            else {
                ps.setNull(parameterIndex, Types.DOUBLE);
            }
        }
        else if(type == Float.class) {
            if (fieldValue != null) {
                ps.setFloat(parameterIndex, (Float) fieldValue);
            }
            else {
                ps.setNull(parameterIndex, Types.FLOAT);
            }
        }
        else if(type == Boolean.class) {
            if (fieldValue != null) {
                ps.setBoolean(parameterIndex, (Boolean) fieldValue);
            }
            else {
                ps.setNull(parameterIndex, Types.BOOLEAN);
            }
        }
        else if (type == java.util.Date.class) {
            if (fieldValue != null) {
                ps.setDate(parameterIndex, new java.sql.Date(((java.util.Date) fieldValue).getTime()));
            } else {
                ps.setNull(parameterIndex, Types.DATE);
            }
        }
    }




}
