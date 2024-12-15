package com.inventory.backend.services.impl;

import com.inventory.backend.entities.User;
import com.inventory.backend.repos.UserRepository;
import com.inventory.backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        userRepository.findAll().forEach(System.out::println);
        return userRepository.findAll();
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
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
