package com.github.salilvnair.ccf.core.dao;

import com.github.salilvnair.ccf.util.common.TypeConvertor;
import com.github.salilvnair.ccf.util.commonutil.lang.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractQueryDao {

    private static final Logger logger = LoggerFactory.getLogger(AbstractQueryDao.class);

    @Autowired
    protected EntityManager entityManager;


    protected List<?> execute(String queryString, Map<String, Object> queryParams) {
        try {
            Query nativeQuery = entityManager.createNativeQuery(queryString);
            for(Map.Entry<String, Object> param : queryParams.entrySet()) {
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
            return nativeQuery.getResultList();
        }
        catch (Exception ex) {
            logger.error("AbstractQueryDao>>execute()>>caught exception:"+ex+ " Query metadata: queryString:["+queryString + "], queryParams:"+queryParams);
        }
        return Collections.emptyList();
    }

    @SuppressWarnings({"unchecked"})
    protected List<Map<String, Object>> execute(String queryString, Map<String, Object> queryParams, boolean transform) {
        List<Map<String, Object>> transformedList = new ArrayList<>();
        try {
            Query nativeQuery = entityManager.createNativeQuery(queryString, Tuple.class);
            for(Map.Entry<String, Object> param : queryParams.entrySet()) {
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
            List<Tuple> tableData = nativeQuery.getResultList();
            for (Tuple row : tableData) {
                List<TupleElement<?>> elements = row.getElements();
                Map<String, Object> rowData = new HashMap<>();
                for (TupleElement<?> element : elements ) {
                    rowData.put(element.getAlias().toUpperCase(), row.get(element.getAlias().toUpperCase()));
                }
                transformedList.add(rowData);
            }
        }
        catch (Exception e) {
            logger.error("AbstractQueryDao>>execute(boolean transform)>>caught exception:"+e+ " Query metadata:queryString:["+queryString + "], queryParams:"+queryParams);
            throw new RuntimeException(e);
        }

        return transformedList;
    }

    protected <T> List<T> execute(String queryString, Class<T> clazz) {
        TypedQuery<T> query = entityManager.createQuery(queryString, clazz);
        return query.getResultList();
    }

    protected <T> List<T> execute(String queryString, Map<String, Object> queryParams, Class<T> clazz, boolean nativeQuery) {
        if(nativeQuery) {
            return execute(queryString, queryParams, clazz);
        }
        TypedQuery<T> query = entityManager.createQuery(queryString, clazz);
        for (Map.Entry<String, Object> param : queryParams.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        return query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    protected <T> List<T> execute(String queryString, Map<String, Object> queryParams, Class<T> clazz) {
        List<T> transformedList = new ArrayList<>();
        try {
            Query nativeQuery = entityManager.createNativeQuery(queryString, Tuple.class);
            for (Map.Entry<String, Object> param : queryParams.entrySet()) {
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
            Map<String, Field> aliasColumnFields = aliasColumnOrIdFields(clazz);
            List<Tuple> tableData = nativeQuery.getResultList();
            for (Tuple row : tableData) {
                List<TupleElement<?>> elements = row.getElements();
                T tObj = ReflectionUtil.createInstance(clazz);
                for (TupleElement<?> element : elements) {
                    if (aliasColumnFields.containsKey(element.getAlias().toUpperCase())) {
                        Field field = aliasColumnFields.get(element.getAlias().toUpperCase());
                        Object dbValue = row.get(element.getAlias().toUpperCase());
                        Object value = dbValue;
                        if (dbValue != null) {
                            Type sourceType = dbValue.getClass();
                            Type destinationType = field.getType();
                            if (sourceType != destinationType) {
                                value = TypeConvertor.convert(dbValue, sourceType, destinationType);
                                if(sourceType == Character.class) {
                                    value = value+"";
                                }
                            }
                        }
                        ReflectionUtil.setField(tObj, field, value);
                    }
                }
                transformedList.add(tObj);
            }
        }
        catch (Exception e) {
            logger.error("AbstractQueryDao>>execute(clazz)>>caught exception:"+e+ " Query metadata: clazz:"+clazz+", queryString:["+queryString + "], queryParams:"+queryParams);
            throw new RuntimeException(e);
        }
        return transformedList;
    }

    protected <T> T executeOne(String queryString, Map<String, Object> queryParams, Class<T> clazz) {
        List<T> results = execute(queryString, queryParams, clazz);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

    private Set<Field> eligibleColumnFields(Class<?> clazz) {
        return Arrays
                .stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toSet());
    }

    private Map<String, Field> aliasColumnOrIdFields(Class<?> clazz) {
        Set<Field> fields = eligibleColumnFields(clazz);
        return fields.stream().collect(Collectors.toMap(field -> field.getAnnotation(Column.class).name().toUpperCase(), field -> field, (o,n) -> n));
    }

}
