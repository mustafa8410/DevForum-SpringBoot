package com.devforum.DeveloperForum.responses;

import com.devforum.DeveloperForum.entities.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    Long userId;
    String jwtToken;
    String refreshToken;
}
