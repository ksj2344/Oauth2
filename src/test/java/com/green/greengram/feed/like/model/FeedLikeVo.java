package com.green.greengram.feed.like.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
/*
    immutable(불변성)하게 객체를 만들고 싶다. 그러면 setter를 빼야함.
    private한 멤버필드에 값을 넣는 방법 setter아니면 생성자.
    생성자를 이용하여 객체생성을 할 때 멤버필드값을 세팅하는 경우의 수가 많을 수 있다. (여기서는 3!+2가지)
    그만큼의 생성자를 일일이 만들어주기는 번거로우므로 Builder를 쓴다.
 */

@Getter
@Builder
@EqualsAndHashCode
public class FeedLikeVo {
    private long feedId;
    private long userId;
    private String createdAt;
}
