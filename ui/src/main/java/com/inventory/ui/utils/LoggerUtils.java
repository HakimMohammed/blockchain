package com.inventory.ui.utils;

public class LoggerUtils {
    public static void logError(String context, Exception e) {
        System.err.println("Error in " + context + ": " + e.getMessage());
        e.printStackTrace();
    }
}
