package com.inventory.backend.mappers;

import com.inventory.backend.dtos.auth.RegisterRequest;
import com.inventory.backend.dtos.user.UserResponse;
import com.inventory.backend.dtos.user.CreateUserRequest;
import com.inventory.backend.dtos.user.UpdateUserRequest;
import com.inventory.backend.entities.User;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public User toEntity(RegisterRequest registerRequest){
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());
        return user;
    }

    // Map CreateUserRequest to User entity
    public User toEntity(CreateUserRequest request, Organization organization) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));  // Convert role to enum
        user.setOrganization(organization);  // Set the organization
        return user;
    }

    // Map UpdateUserRequest to existing User entity
    public User toEntity(UpdateUserRequest request, User existingUser, Organization organization) {
        if (request.getFirstName() != null) existingUser.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existingUser.setLastName(request.getLastName());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getPassword() != null) existingUser.setPassword(request.getPassword());
        if (request.getRole() != null) existingUser.setRole(Role.valueOf(request.getRole().toUpperCase()));
        if (organization != null) existingUser.setOrganization(organization);
        return existingUser;
    }

    // Map User entity to UserResponse DTO
    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getOrganization() != null ? user.getOrganization() : null
        );
    }

    // Map list of User entities to list of UserResponse DTOs
    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
