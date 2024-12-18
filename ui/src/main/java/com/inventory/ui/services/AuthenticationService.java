package com.inventory.ui.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.ui.auth.AuthSession;
import com.inventory.ui.dtos.LoginRequest;
import okhttp3.Response;

public class AuthenticationService {
    private final HttpService httpService;

    public AuthenticationService() {
        this.httpService = new HttpService();
    }

    public boolean login(String email, String password) {
        try {
            LoginRequest loginRequest = LoginRequest.builder().email(email).password(password).build();
            Response response = httpService.post("auth/login", loginRequest);

            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseBody = response.body().string();
                AuthSession authSession = objectMapper.readValue(responseBody, AuthSession.class);
                TokenStorageService.saveToken(authSession.getAccessToken());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean logout() {
        try {
            String accessToken = TokenStorageService.loadToken();
            if (accessToken.isEmpty()) {
                System.out.println("No token found. User is already logged out.");
                return false;
            }

            Response response = httpService.post("auth/logout",  null);
            if (response.isSuccessful()) {
                TokenStorageService.clearToken();
                System.out.println("User logged out successfully.");
                return true;
            } else {
                System.out.println("Logout failed: " + response.body().string());
            }
        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
        }
        return false;
    }
}