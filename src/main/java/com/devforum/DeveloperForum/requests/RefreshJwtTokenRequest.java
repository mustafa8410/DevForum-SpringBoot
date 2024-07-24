package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class RefreshJwtTokenRequest {
    Long userId;
    String refreshToken;
}
