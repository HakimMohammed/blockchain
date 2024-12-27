package com.inventory.backend.services.impl;

import com.inventory.backend.dtos.user.UserResponse;
import com.inventory.backend.entities.User;
import com.inventory.backend.mappers.UserMapper;
import com.inventory.backend.repos.UserRepository;
import com.inventory.backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> findAll() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateById(UUID id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User save(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
