package com.github.salilvnair.ccf.core.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.Map;

@Component
@Transactional
public class UpdateQueryService extends AbstractQueryDao {

    @Transactional
    public int update(String tableName, Map<String, Object> fieldNameValueMap,  Map<String, Object> whereClauseMap) {
        // Generate the SQL query dynamically
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE ").append(tableName).append(" SET ");

        // Build placeholders for values
        for (String fieldName : fieldNameValueMap.keySet()) {
            queryBuilder.append(fieldName).append("=").append(":").append(fieldName).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);

        queryBuilder.append(" WHERE ");
        for (String fieldName : whereClauseMap.keySet()) {
            queryBuilder.append(fieldName).append("=").append(":").append(fieldName);
            queryBuilder.append(" and ");
        }
        queryBuilder.delete(queryBuilder.length() - 5, queryBuilder.length());
        // Create and execute the query
        String sql = queryBuilder.toString();
        Query query = entityManager.createNativeQuery(sql);

        // Set parameters
        for (Map.Entry<String, Object> entry : fieldNameValueMap.entrySet()) {
            if(entry.getValue() instanceof Date) {
                query.setParameter(entry.getKey(), (Date)entry.getValue(), TemporalType.DATE);
            }
            else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<String, Object> entry : whereClauseMap.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Execute the query
        return query.executeUpdate();
    }


    @Transactional
    public int insert(String tableName, Map<String, Object> fieldNameValueMap) {
        // Generate the SQL query dynamically
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ").append(tableName).append(" (");

        // Build the column names
        for (String fieldName : fieldNameValueMap.keySet()) {
            queryBuilder.append(fieldName).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Remove the trailing comma
        queryBuilder.append(") VALUES (");

        // Build placeholders for values
        for (String fieldName : fieldNameValueMap.keySet()) {
            queryBuilder.append(":").append(fieldName).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Remove the trailing comma
        queryBuilder.append(")");

        // Create and execute the query
        String sql = queryBuilder.toString();
        Query query = entityManager.createNativeQuery(sql);

        // Set parameters
        for (Map.Entry<String, Object> entry : fieldNameValueMap.entrySet()) {
//            if (entry.getValue() == null) {
//                query.setParameter(entry.getKey(), new TypedParameterValue(StandardBasicTypes.LONG, null));
//            }
            if(entry.getValue() instanceof Date) {
                query.setParameter(entry.getKey(), (Date)entry.getValue(), TemporalType.DATE);
            }
            else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // Execute the query
        return query.executeUpdate();
    }

    @Transactional
    public Long nextValue(String sequenceName) {
//        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL"; // For Oracle
        String sql = "SELECT nextval('" + sequenceName + "')";
        return ((Number)entityManager.createNativeQuery(sql).getSingleResult()).longValue();
    }
}
