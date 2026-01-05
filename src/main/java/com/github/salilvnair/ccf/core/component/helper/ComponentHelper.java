package com.github.salilvnair.ccf.core.component.helper;

import com.github.salilvnair.ccf.core.component.model.core.IComponentDataRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentHelper {

    private ComponentHelper() {}

    @SuppressWarnings("unchecked")
    public static  <T> T extract(IComponentDataRequest request) {
        return (T)request;
    }

    @SuppressWarnings("unchecked")
    public static <T> T extract(String key, Map<String, Object> inputParams) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            return (T)inputParams.get(key);
        }
        return null;
    }

    public static Integer extractInteger(String key, Map<String, Object> inputParams) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key) && inputParams.get(key) instanceof Number) {
            return Integer.parseInt(inputParams.get(key)+"");
        }
        return null;
    }

    public static Long extractLong(String key, Map<String, Object> inputParams) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key) && inputParams.get(key) instanceof Number) {
            return Long.parseLong(inputParams.get(key)+"");
        }
        return null;
    }

    public static void addParam(String key, Object value, Map<String, Object> inputParams) {
        if(!CollectionUtils.isEmpty(inputParams)) {
            inputParams.put(key, value);
        }
    }

    public static <T> T extractObject(String key, Map<String, Object> inputParams, Class<T> clazz ) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            ObjectMapper mapper = new ObjectMapper();
            Object object = inputParams.get(key);
            return mapper.convertValue(object, clazz);
        }
        return null;
    }

    public static <T> T extractObjectOrDefault(String key, Map<String, Object> inputParams, Class<T> clazz, T defaultValue ) {
        T extractedObject = null;
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            ObjectMapper mapper = new ObjectMapper();
            Object object = inputParams.get(key);
            try {
                extractedObject =  mapper.convertValue(object, clazz);
            }
            catch (Exception ignore){
                extractedObject = defaultValue;
            }
        }
        return extractedObject;
    }

    public static <T> List<T> extractList(String key, Map<String, Object> inputParams, Class<T> clazz ) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            ObjectMapper mapper = new ObjectMapper();
            Object object = inputParams.get(key);
            return mapper.convertValue(object,  mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        }
        return null;
    }

    public static <K,V> Map<K,V> extractObjectValueMap(String key, Map<String, Object> inputParams, Class<K> kClass, Class<V> vClass ) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            ObjectMapper mapper = new ObjectMapper();
            Object object = inputParams.get(key);
            return mapper.convertValue(object,  mapper.getTypeFactory().constructMapLikeType(Map.class, kClass, vClass));
        }
        return null;
    }

    public static <K,V> Map<K, List<V>> extractListValueMap(String key, Map<String, Object> inputParams, Class<K> kClass, Class<V> vClass ) {
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            ObjectMapper mapper = new ObjectMapper();
            Object object = inputParams.get(key);
            return mapper.convertValue(object, mapper.getTypeFactory().constructMapType(Map.class,  mapper.getTypeFactory().constructType(kClass), mapper.getTypeFactory().constructCollectionType(List.class, vClass)));
        }
        return null;
    }

    public static <T> List<T> safeExtractList(String key, Map<String, Object> inputParams, Class<T> clazz ) {
        List<T> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key)) {
            ObjectMapper mapper = new ObjectMapper();
            Object object = inputParams.get(key);
            try {
                list = mapper.convertValue(object,  mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            catch (Exception ignore){}
        }
        return list;
    }

    public static boolean hasParam(String key, Map<String, Object> inputParams) {
        return !CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(key);
    }
}
