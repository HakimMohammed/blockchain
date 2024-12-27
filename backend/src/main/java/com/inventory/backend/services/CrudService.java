package com.inventory.backend.services;

import java.util.List;
import java.util.UUID;

public interface CrudService<T , R> {
    List<R> findAll();
    T findById(UUID id);
    T updateById(UUID id , T t);
    T save(T t);
    void deleteById(UUID id);
}
