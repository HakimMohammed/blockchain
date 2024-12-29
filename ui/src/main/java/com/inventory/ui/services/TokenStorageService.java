package com.inventory.ui.services;

import java.io.*;
import java.nio.file.*;

public class TokenStorageService {
    private static final String TOKEN_FILE_PATH = "token.txt";

    public static void saveToken(String token) throws IOException {
        Files.write(Paths.get(TOKEN_FILE_PATH), token.getBytes());
    }

    public static String loadToken() throws IOException {
        if (Files.exists(Paths.get(TOKEN_FILE_PATH))) {
            return new String(Files.readAllBytes(Paths.get(TOKEN_FILE_PATH)));
        }
        return "";
    }

    public static void clearToken() throws IOException {
        Files.deleteIfExists(Paths.get(TOKEN_FILE_PATH));
    }
}
