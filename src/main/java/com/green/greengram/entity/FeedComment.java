package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedComment extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedCommentId;

    @ManyToOne(fetch = FetchType.LAZY) //일단 안가져오고 필요하면 가져오겠다
    @JoinColumn(name="feed_id",nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY) //기본값 EAGER
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(length = 150, nullable = false)
    private String comment;
}
