package com.devforum.DeveloperForum.entities;

import com.devforum.DeveloperForum.enums.HelpfulRank;
import com.devforum.DeveloperForum.enums.ReputationRank;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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

    Long interactionCount;
    Long helpfulCount;

    public void checkForRepRankUpgrade(){
        if(interactionCount == 100)
            setReputationRank(ReputationRank.RISING_STAR);

        else if(interactionCount == 300)
            setReputationRank(ReputationRank.RESPECTED);

        else if(interactionCount == 500)
            setReputationRank(ReputationRank.LEGEND);

    }

    public void checkForHelpfulRankUpgrade(){
        if(helpfulCount == 50)
            setHelpfulRank(HelpfulRank.HELPER);

        else if(helpfulCount == 100)
            setHelpfulRank(HelpfulRank.ADVISOR);

        else if(helpfulCount == 150)
            setHelpfulRank(HelpfulRank.SUPPORT_SPECIALIST);
        else if(helpfulCount == 200)
            setHelpfulRank(HelpfulRank.PROBLEM_SOLVER);
        else if(helpfulCount == 250)
            setHelpfulRank(HelpfulRank.SOLUTION_GURU);
        else if(helpfulCount == 500)
            setHelpfulRank(HelpfulRank.GUIDING_STAR);
        else if(helpfulCount == 1000)
            setHelpfulRank(HelpfulRank.SUPPORT_LEGEND);
    }
}
