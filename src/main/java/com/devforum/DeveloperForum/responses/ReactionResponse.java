package com.devforum.DeveloperForum.responses;

import com.devforum.DeveloperForum.entities.CommentReaction;
import com.devforum.DeveloperForum.entities.PostReaction;
import com.devforum.DeveloperForum.enums.ReactionType;
import lombok.Data;

@Data
public class ReactionResponse {
    String reactionTo;
    Long reactionId;
    ReactionType reactionType;
    Long reactedEntityId;
    Long reactorId;

    public ReactionResponse(CommentReaction entity){
        this.reactionTo = "comment";
        this.reactionId = entity.getId();
        this.reactionType = entity.getReactionType();
        this.reactedEntityId = entity.getComment().getId();
        this.reactorId = entity.getReactorId();
    }

    public ReactionResponse(PostReaction entity){
        this.reactionTo = "post";
        this.reactionId = entity.getId();
        this.reactionType = entity.getReactionType();
        this.reactedEntityId = entity.getPost().getId();
        this.reactorId = entity.getReactorId();
    }
}
