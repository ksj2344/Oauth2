package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserFollow extends CreatedAt {
    @EmbeddedId
    private UserFollowIds userFollowIds; //복합키 지정

    @ManyToOne  //M:1 관계
    @MapsId("fromUserId") //UserFollowIds 참조걸기. 멤버필드 이름 가져와야함
    @JoinColumn(name="from_user_id") //관계설정하였다면 JoinColumn. 복합키 설정시 이름 이상하게 설정되는 이슈가 있어 name= 지정
    private User fromUser;

    @ManyToOne
    @MapsId("toUserId")
    @JoinColumn(name="to_user_id")
    private User toUser;
}
