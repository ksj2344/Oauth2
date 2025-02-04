package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedLike extends CreatedAt {
    @EmbeddedId
    private FeedLikeIds feedLikeIds; //복합키 지정

    @ManyToOne
    @MapsId("feedId") //FeedLikeIds 참조걸기. 멤버필드 이름 가져와야함
    @JoinColumn(name="feed_id")
    private Feed FeedId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User userId;
}
