package com.auth.user_management.utils;

import java.util.Base64;

public class Common {
    public static String convertToBase64(String input) {
        try {
            return Base64.getEncoder().encodeToString(input.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
