package com.green.greengram.feed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
/*
    feed_pics 테이블에 튜플 여러개를 insert 한문장으로 처리하기 위해 사용하는 객체
    FeedPostRes와 구조가 같다. 그러나 사용 용도가 다르고 부모관계가 확실하지 않기 때문에 분리해서 사용하는 것이 좋다.
*/
public class FeedPicDto {
    private long feedId;
    private List<String> pics;
}
