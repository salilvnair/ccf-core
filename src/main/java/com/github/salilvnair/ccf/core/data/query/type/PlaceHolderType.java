package com.github.salilvnair.ccf.core.data.query.type;

public enum PlaceHolderType {
    PICK_FROM_COMMON_QUERY("[PICK_FROM_COMMON_QUERY]"),
    ORDER_BY_PLACE_HOLDER("[ORDER_BY_PLACE_HOLDER]"),
    ROW_NUMBER_ORDER_BY_PLACE_HOLDER("[ROW_NUMBER_ORDER_BY_PLACE_HOLDER]"),
    FILTER_BY_FIELD_PLACEHOLDER("[FILTER_BY_FIELD_PLACEHOLDER]"),
    WHERE_FILTER_BY_PLACE_HOLDER("[WHERE_FILTER_BY_PLACE_HOLDER]"),
    FETCH_FIRST_N_ROWS_PLACE_HOLDER("[FETCH_FIRST_N_ROWS_PLACE_HOLDER]"),
    MULTI_CONDITION_WHERE_FILTER_BY_PLACE_HOLDER("[MULTI_CONDITION_WHERE_FILTER_BY_PLACE_HOLDER]");

    private final String type;
    PlaceHolderType(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }
}
