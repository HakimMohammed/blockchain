package com.inventory.ui.services;

public class AuthenticationService {
    public boolean authenticate(String username, String password) {
        // This is a mock authentication
        // In a real application, you would implement actual authentication logic here
        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty();
    }
}