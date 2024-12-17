package com.inventory.ui.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Env {
    private static final Map<String, String> envVariables = new HashMap<>();
    private static final String ENV_FILE_PATH = "./.env";

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().startsWith("#") && line.contains("=")) { // Skip comments
                    String[] parts = line.split("=", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    envVariables.put(key, value);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading .env file: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return envVariables.get(key);
    }
}

