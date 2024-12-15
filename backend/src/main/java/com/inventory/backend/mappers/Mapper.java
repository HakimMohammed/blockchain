package com.inventory.backend.mappers;

import java.util.List;

public interface Mapper<T , C , U , R> {
    T toEntity(C C);
    T toEntity(U u , T t);
    R toResponse(T t);
    List<R> toResponseList(List<T> list);
}
