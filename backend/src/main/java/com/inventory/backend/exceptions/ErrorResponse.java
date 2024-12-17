package com.inventory.backend.exceptions;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
    private final String type;
    private final Map<String, String> details;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
