package com.green.greengram.feed.model;


import com.green.greengram.feed.comment.model.FeedCommentGetRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FeedGetRes {
    private long feedId;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private String contents;
    private String location;
    private String createdAt;
    private int isLike;

    private List<String> pics;
    private FeedCommentGetRes comment;

    public FeedGetRes(FeedWithPicCommentDto dto) {
        feedId = dto.getFeedId();
        writerUserId = dto.getWriterUserId();
        writerNm = dto.getWriterNm();
        writerPic = dto.getWriterPic();
        contents = dto.getContents();
        location = dto.getLocation();
        createdAt = dto.getCreatedAt();
        isLike = dto.getIsLike();
        pics = dto.getPics();
        FeedCommentGetRes commentGetRes = new FeedCommentGetRes();
        commentGetRes.setMoreComment(dto.getCommentList().size()==4?true:false);
        commentGetRes.setCommentList(dto.getCommentList());
        if(commentGetRes.isMoreComment()){
            commentGetRes.getCommentList().remove(commentGetRes.getCommentList().size()-1);
        }
        comment = commentGetRes;
    }
}
