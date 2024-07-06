package com.devforum.DeveloperForum.entities;

import com.devforum.DeveloperForum.enums.HelpfulRank;
import com.devforum.DeveloperForum.enums.ReputationRank;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;
    String name;
    String username;
    String password;

    @Temporal(TemporalType.DATE)
    LocalDate registerDate;

    @Enumerated(EnumType.STRING)
    ReputationRank reputationRank;

    @Enumerated(EnumType.STRING)
    HelpfulRank helpfulRank;
}
