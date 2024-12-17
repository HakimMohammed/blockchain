package com.inventory.ui.utils;

public class ValidationUtils {
    public static boolean validateLoginInput(String username, String password) {
        return username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty();
    }
}
