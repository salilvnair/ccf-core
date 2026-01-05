package com.github.salilvnair.ccf.util.commonutil.lang;

public class StringUtils {
    private StringUtils() {}

    public static String limitString(String str, int i) {
        return str == null ? null : str.length() > i ? str.substring(0, i) : str;
    }
}
