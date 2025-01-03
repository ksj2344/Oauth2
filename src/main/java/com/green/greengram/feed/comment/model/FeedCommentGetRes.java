package com.green.greengram.feed.comment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class FeedCommentGetRes{
    private boolean moreComment;
    private List<FeedCommentDto> commentList;

    // moreComment와 comment는 1:N 관계
    // 댓글 리스트 정보를 넘길때 더보기 활성화 여부도 넘겨야함
}
