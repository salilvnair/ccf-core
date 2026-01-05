package com.github.salilvnair.ccf.core.constant;

public class StringConstant {
    private StringConstant() {}

    public static final String Y = "Y";
    public static final String C = "C";
    public static final String P = "P";
    public static final String I = "I";
    public static final String N = "N";
    public static final String S = "S";
    public static final String F = "F";
    public static final String FL = "FL";
    public static final String O = "O";
    public static final String VF = "VF";
    public static final String TG = "TG";
    public static final String D = "D";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TEN = "10";
    public static final String N_500 = "500";
    public static final String N_20 = "20";
    public static final String EQUALS_STRING = "=";
    public static final String GREATER_THAN_STRING = ">";
    public static final String EQUAL = "=";
    public static final String SINGLE_QUOTE = "'";
    public static final String AND = "and";
    public static final String SPACE = " ";




    public static boolean nullOrValue(String value, String source) {
        return source == null || value.equals(source);
    }

    public static boolean nullOrValueIgnoreCase(String value, String source) {
        return source == null || value.equalsIgnoreCase(source);
    }

    public static String returnEmptyOrValue(String value) {
        return value == null ? "" : value;
    }

    public static String returnEmptyOrValue(String value, String suffixString) {
        return value == null ? "" : value + suffixString;
    }
}
