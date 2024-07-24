package com.devforum.DeveloperForum.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    Long userId;
    String jwtToken;
}
