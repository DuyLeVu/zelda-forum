package com.zelda.model.entity;

import javax.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Date createAt;

//    private Long likes;

//    @OneToMany(targetEntity = Comment.class, cascade = {CascadeType.ALL})
//    private List<Comment> childrenComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
