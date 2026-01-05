package com.github.salilvnair.ccf.util.commonutil.file;

import java.io.File;
import java.util.regex.Pattern;

public class FilenameUtils {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String EMPTY_STRING = "";
    private static final int NOT_FOUND = -1;
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR;
    private static final char OTHER_SEPARATOR;
    private static final Pattern IPV4_PATTERN;
    private static final int IPV4_MAX_OCTET_VALUE = 255;
    private static final int IPV6_MAX_HEX_GROUPS = 8;
    private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;
    private static final int MAX_UNSIGNED_SHORT = 65535;
    private static final int BASE_16 = 16;
    private static final Pattern REG_NAME_PART_PATTERN;

    public static String removeExtension(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            requireNonNullChars(fileName);
            int index = indexOfExtension(fileName);
            return index == -1 ? fileName : fileName.substring(0, index);
        }
    }

    private static void requireNonNullChars(String path) {
        if (path.indexOf(0) >= 0) {
            throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
    }

    static boolean isSystemWindows() {
        return  File.separatorChar == '\\';
    }

    static {
        SYSTEM_SEPARATOR = File.separatorChar;
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }

        IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");
    }

    private static int getAdsCriticalOffset(String fileName) {
        int offset1 = fileName.lastIndexOf(SYSTEM_SEPARATOR);
        int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
        if (offset1 == -1) {
            return offset2 == -1 ? 0 : offset2 + 1;
        } else {
            return offset2 == -1 ? offset1 + 1 : Math.max(offset1, offset2) + 1;
        }
    }

    public static int indexOfExtension(String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return -1;
        } else {
            if (isSystemWindows()) {
                int offset = fileName.indexOf(58, getAdsCriticalOffset(fileName));
                if (offset != -1) {
                    throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
                }
            }

            int extensionPos = fileName.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(fileName);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    public static int indexOfLastSeparator(String fileName) {
        if (fileName == null) {
            return -1;
        } else {
            int lastUnixPos = fileName.lastIndexOf(47);
            int lastWindowsPos = fileName.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }
}
