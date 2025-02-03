package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class FeedPic extends CreatedAt{
    @EmbeddedId //복합키 지정
    private FeedPicIds feedPicIds;

    @ManyToOne
    @MapsId("feedId") //FeedPicIds의 속성명
    @JoinColumn(name="feed_id")
    @OnDelete(action = OnDeleteAction.CASCADE) //단방향으로 ON THE CASCADE 설정 //테이블 만들 때 적용된다(DDL)
    private Feed feed;
}
