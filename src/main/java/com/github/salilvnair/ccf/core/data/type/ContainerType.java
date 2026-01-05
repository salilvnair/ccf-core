package com.github.salilvnair.ccf.core.data.type;

import java.util.Arrays;
import java.util.Optional;

public enum ContainerType {
    MULTI_ROW(Value.MULTI_ROW),
    FILTER_VALUE_MULTI_ROW(Value.FILTER_VALUE_MULTI_ROW),
    MULTI_INFO(Value.MULTI_INFO),
    DATA_TABLE(Value.DATA_TABLE, true),
    SIMPLE_DATA_TABLE(Value.SIMPLE_DATA_TABLE, true),
    INFO_TABLE(Value.INFO_TABLE, true),
    INFO(Value.INFO);



    public static class Value {
        public static final String MULTI_ROW = "MULTI_ROW";
        public static final String FILTER_VALUE_MULTI_ROW = "FILTER_VALUE_MULTI_ROW";
        public static final String MULTI_INFO = "MULTI_INFO";
        public static final String DATA_TABLE = "DATA_TABLE";
        public static final String SIMPLE_DATA_TABLE = "SIMPLE_DATA_TABLE";
        public static final String INFO_TABLE = "INFO_TABLE";
        public static final String INFO = "INFO";
    }

    private final String type;
    private final boolean isTable;

    public String value() {
        return type;
    }

    public boolean isTable() {
        return isTable;
    }





    ContainerType(String type, boolean isTable) {
        this.type = type;
        this.isTable = isTable;
    }

    ContainerType(String type) {
        this.type = type;
        this.isTable = false;
    }

    public static ContainerType type(String name) {
        Optional<ContainerType> typeEnum = Arrays.stream(ContainerType.values())
                .filter(comp -> comp.value() != null && comp.value().equals(name)).findFirst();
        return typeEnum.orElse(null);
    }
}
