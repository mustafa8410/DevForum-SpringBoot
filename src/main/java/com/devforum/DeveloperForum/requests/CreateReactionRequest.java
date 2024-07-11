package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class CreateReactionRequest {
    String reactionTo;
    String reactionType;
    Long reactedEntityId;
    Long reactorId;
}
