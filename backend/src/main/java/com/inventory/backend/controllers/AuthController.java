package com.inventory.backend.controllers;

import com.inventory.backend.config.jwt.CustomUserDetails;
import com.inventory.backend.config.jwt.JwtTokenUtil;
import com.inventory.backend.dtos.auth.LoginRequest;
import com.inventory.backend.dtos.auth.LoginResponse;
import com.inventory.backend.dtos.auth.RegisterRequest;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.User;
import com.inventory.backend.repos.OrganizationRepository;
import com.inventory.backend.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String jwt = jwtTokenUtil.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new LoginResponse(
                jwt,
                userDetails.getUser().getId(),
                userDetails.getUser().getRole()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        // Find organization (assuming organization ID is provided)
        Organization organization = organizationRepository.findById(registerRequest.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // Create new user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setOrganization(organization);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}

