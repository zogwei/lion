package com.alacoder.lion.tcc.utils;

public class StringUtils {

    public static boolean isNotEmpty(String value) {

        if (value == null) {
            return false;
        }

        if (value.equals("")) {
            return false;
        }

        return true;
    }
}
