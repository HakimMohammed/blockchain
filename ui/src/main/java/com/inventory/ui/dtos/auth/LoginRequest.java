package com.inventory.ui.dtos.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
