package com.github.salilvnair.ccf.core.type;

import java.util.Arrays;
import java.util.Optional;

public enum UserRoleType {
    USER(Value.USER),
    TESTER(Value.TESTER),
    DEVELOPER(Value.DEVELOPER),
    ;



    public static class Value {
        public static final String USER = "USER";
        public static final String TESTER = "TESTER";
        public static final String DEVELOPER = "DEVELOPER";
    }

    private final String type;

    public String value() {
        return type;
    }



    UserRoleType(String type) {
        this.type = type;
    }

    public static UserRoleType type(String name) {
        Optional<UserRoleType> typeEnum = Arrays.stream(UserRoleType.values())
                .filter(comp -> comp.value() != null && comp.value().equals(name)).findFirst();
        return typeEnum.orElse(null);
    }
}
