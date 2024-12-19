package com.inventory.ui.services;

import com.inventory.ui.models.User;

public interface AuthenticationService {
    boolean login(String email, String password);

    boolean register(User user);

    boolean logout();
}
