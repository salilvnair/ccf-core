package com.github.salilvnair.ccf.util.commonutil.email;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(".*@([^@]+)$");

    public static String extractDomain(String email) {
        if (email == null) {
            return null;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        else {
            return null;
        }
    }
}