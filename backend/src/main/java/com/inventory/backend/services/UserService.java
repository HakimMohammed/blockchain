package com.inventory.backend.services;

import com.inventory.backend.dtos.user.UserResponse;
import com.inventory.backend.entities.User;

public interface UserService extends CrudService<User , UserResponse> {
}
