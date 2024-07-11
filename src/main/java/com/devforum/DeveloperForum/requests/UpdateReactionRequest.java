package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class UpdateReactionRequest {
    String reactedTo;
    String newReactionType;
}
