package com.inventory.ui.utils;

public class ValidationUtils {
    public static boolean validateLoginInput(String username, String password) {
        return username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty();
    }

    public static boolean isValidPrice(String price) {
        try {
            double value = Double.parseDouble(price);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidQuantity(String quantity) {
        try {
            int value = Integer.parseInt(quantity);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
