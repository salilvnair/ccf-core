package com.github.salilvnair.ccf.core.data.type;

import java.util.Arrays;
import java.util.Optional;

public enum SectionType {
    COMMON(Value.COMMON),
    DATA_TABLE_ROW_COUNT(Value.DATA_TABLE_ROW_COUNT),
    ;



    public static class Value {
        public static final String COMMON = "COMMON";
        public static final String DATA_TABLE_ROW_COUNT = "DATA_TABLE_ROW_COUNT";
    }

    private final String type;

    public String value() {
        return type;
    }



    SectionType(String type) {
        this.type = type;
    }

    public static SectionType type(String name) {
        Optional<SectionType> typeEnum = Arrays.stream(SectionType.values())
                .filter(comp -> comp.value() != null && comp.value().equals(name)).findFirst();
        return typeEnum.orElse(null);
    }
}
