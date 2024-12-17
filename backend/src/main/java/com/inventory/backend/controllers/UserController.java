package com.inventory.backend.controllers;

import com.inventory.backend.dtos.user.CreateUserRequest;
import com.inventory.backend.dtos.user.UpdateUserRequest;
import com.inventory.backend.dtos.user.UserResponse;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.User;
import com.inventory.backend.mappers.UserMapper;
import com.inventory.backend.services.OrganizationService;
import com.inventory.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponse> userResponses = userMapper.toResponseList(users);
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        User user = userService.findById(id);
        UserResponse userResponse = userMapper.toResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody @Valid CreateUserRequest createUserRequest) {
        Optional<Organization> organization = Optional.ofNullable(organizationService.findById(createUserRequest.getOrganizationId()));
        User user = userMapper.toEntity(createUserRequest, organization.get());
        User savedUser = userService.save(user);
        UserResponse userResponse = userMapper.toResponse(savedUser);
        return new ResponseEntity<>(userResponse , HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateById(@PathVariable UUID id, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.findById(id);
        Optional<Organization> organization = Optional.ofNullable(organizationService.findById(updateUserRequest.getOrganizationId()));
        if (organization.isEmpty())
            user = userService.updateById(id, userMapper.toEntity(updateUserRequest, user, null));
        else user = userMapper.toEntity(updateUserRequest, user, organization.get());
        User updatedUser = userService.updateById(id, user);
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
