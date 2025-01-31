package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedPic extends CreatedAt{
    @EmbeddedId //복합키 지정
    private FeedPicIds feedPicIds;

    @ManyToOne
    @MapsId("feedId") //FeedPicIds의 속성명
    @JoinColumn(name="feed_id")
    private Feed feed;


}
