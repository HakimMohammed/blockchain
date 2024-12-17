package com.inventory.backend.dtos.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class LoginRequest {
    private String email;
    private String password;
}
