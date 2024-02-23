package com.zelda.util;

public class Utils {
    public static String trimToEmpty(String str){
        return str == null ? "" : str.trim();
    }
    public static boolean stringIsEmpty(CharSequence cs){
        return cs == null || cs.length() == 0;
    }
}
