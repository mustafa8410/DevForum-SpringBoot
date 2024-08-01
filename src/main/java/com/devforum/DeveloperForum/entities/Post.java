package com.devforum.DeveloperForum.entities;

import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.Date;

@Entity
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    User user;

    String title;

    @Lob
    @Column(columnDefinition = "text")
    String text;

    @Enumerated(EnumType.STRING)
    PostCategory postCategory;
    @Enumerated(EnumType.STRING)
    PostTag postTag;

    @Temporal(TemporalType.TIMESTAMP)
    Date postDate;

    Long numberOfReactions;


}
