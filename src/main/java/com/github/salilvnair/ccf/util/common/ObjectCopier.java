package com.github.salilvnair.ccf.util.common;

import com.github.salilvnair.ccf.util.commonutil.lang.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ObjectCopier {

    private ObjectCopier() {}

    public static <T> T clone(T object, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String objectString = mapper.writeValueAsString(object);
        return mapper.readValue(objectString, clazz);
    }

    public static <T> List<T> clone(List<T> objectList, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String objectString = mapper.writeValueAsString(objectList);
        return ReflectionUtil.toGenericListEntity(clazz, objectString);
    }

}
