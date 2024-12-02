package com.green.greengramver2.feed.comment.model;

import lombok.Getter;
import lombok.Setter;

//Value Object
//DataTransferObject
@Getter
@Setter
public class FeedCommentDto {
    private String comment;
    private long feedCommentId;
    private long writerUserId;
    private String writerNM;
    private String writerPic;
}
