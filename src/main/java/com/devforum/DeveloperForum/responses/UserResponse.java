package com.devforum.DeveloperForum.responses;

import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.enums.HelpfulRank;
import com.devforum.DeveloperForum.enums.ReputationRank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    Long id;
    String name;
    String username;
    LocalDate registerDate;
    ReputationRank reputationRank;
    HelpfulRank helpfulRank;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.registerDate = entity.getRegisterDate();
        this.reputationRank = entity.getReputationRank();
        this.helpfulRank = entity.getHelpfulRank();
    }
}
