package com.inventory.backend.services;

import com.inventory.backend.repos.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType("application/json");
      try {
        response.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
      } catch (Exception e) {
        throw new RuntimeException("Failed to write response", e);
      }
      return;
    }

    final String jwt = authHeader.substring(7);
    var storedToken = tokenRepository.findByToken(jwt).orElse(null);

    if (storedToken == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      try {
        response.getWriter().write("{\"error\": \"Token not found\"}");
      } catch (Exception e) {
        throw new RuntimeException("Failed to write response", e);
      }
      return;
    }

    if (storedToken.isExpired() || storedToken.isRevoked()) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      try {
        response.getWriter().write("{\"error\": \"Token is expired or revoked\"}");
      } catch (Exception e) {
        throw new RuntimeException("Failed to write response", e);
      }
      return;
    }

    storedToken.setExpired(true);
    storedToken.setRevoked(true);
    tokenRepository.save(storedToken);
    SecurityContextHolder.clearContext();
  }
}
