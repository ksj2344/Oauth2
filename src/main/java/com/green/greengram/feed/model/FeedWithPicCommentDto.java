package com.green.greengram.feed.model;

import com.green.greengram.feed.comment.model.FeedCommentDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedWithPicCommentDto {
    private long feedId;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private String contents;
    private String location;
    private String createdAt;
    private int isLike;
    private List<String> pics;
    private List<FeedCommentDto> commentList;
}
