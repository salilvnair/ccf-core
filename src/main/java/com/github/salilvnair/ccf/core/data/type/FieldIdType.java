package com.github.salilvnair.ccf.core.data.type;

import java.util.Arrays;
import java.util.Optional;

public enum FieldIdType {
    ID(Id.ID),
    READ_ONLY(Id.READ_ONLY),
    TEXT(Id.TEXT),
    DATE(Id.DATE),
    BUTTON(Id.BUTTON),
    DATE_STRING(Id.DATE_STRING),
    CONTAINER(Id.CONTAINER),
    DATE_TEXT(Id.DATE_TEXT),
    INPUT_TEXT(Id.INPUT_TEXT),
    INPUT_SELECT(Id.INPUT_SELECT),
    INPUT_EMAIL(Id.INPUT_EMAIL),
    INPUT_PHONE(Id.INPUT_PHONE),
    INPUT_DATE(Id.INPUT_DATE),

    ;



    public static class Id {
        public static final int ID = -1;
        public static final int READ_ONLY = 0;
        public static final int TEXT = 1;
        public static final int DATE = 2;
        public static final int BUTTON = 3;
        public static final int DATE_STRING = 4;
        public static final int CONTAINER = 5;
        public static final int DATE_TEXT = 6;
        public static final int INPUT_TEXT = 7;
        public static final int INPUT_EMAIL = 8;
        public static final int INPUT_PHONE = 9;
        public static final int INPUT_SELECT = 10;
        public static final int INPUT_DATE = 11;
        public static final int HYPERLINK_TEXT = 12;

    }

    private final int typeId;

    public int id() {
        return typeId;
    }



    FieldIdType(int typeId) {
        this.typeId = typeId;
    }

    public static FieldIdType type(int id) {
        Optional<FieldIdType> typeEnum = Arrays.stream(FieldIdType.values())
                .filter(comp -> comp.id() == id).findFirst();
        return typeEnum.orElse(null);
    }
}
