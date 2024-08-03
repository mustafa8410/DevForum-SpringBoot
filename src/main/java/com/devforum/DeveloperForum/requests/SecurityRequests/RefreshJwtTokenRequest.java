package com.devforum.DeveloperForum.requests.SecurityRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshJwtTokenRequest {
    Long userId;
    String refreshToken;
    String expiredJwtToken;
}
