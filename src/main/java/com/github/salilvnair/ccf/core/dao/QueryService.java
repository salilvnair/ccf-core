package com.github.salilvnair.ccf.core.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QueryService extends AbstractQueryDao {
    public  <T> List<T> execute(String queryString, Map<String, Object> queryParams, Class<T> clazz) {
        return super.execute(queryString, queryParams, clazz);
    }
}
