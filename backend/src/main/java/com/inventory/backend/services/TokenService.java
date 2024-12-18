package com.inventory.backend.services;

public interface TokenService {
    Boolean isTokenBlackListed(String token);
    void invalidateToken(String token);
}
