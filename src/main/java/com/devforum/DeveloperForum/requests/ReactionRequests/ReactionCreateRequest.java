package com.devforum.DeveloperForum.requests.ReactionRequests;

import lombok.Data;

@Data
public class ReactionCreateRequest {
    String reactionTo;
    String reactionType;
    Long reactedEntityId;
    Long reactorId;
}
