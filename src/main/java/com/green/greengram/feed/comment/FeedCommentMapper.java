package com.green.greengram.feed.comment;

import com.green.greengram.feed.comment.model.FeedCommentDelReq;
import com.green.greengram.feed.comment.model.FeedCommentDto;
import com.green.greengram.feed.comment.model.FeedCommentGetReq;
import com.green.greengram.feed.comment.model.FeedCommentPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedCommentMapper {
    int insFeedComment(FeedCommentPostReq p);
    List<FeedCommentDto> selFeedCommentList(FeedCommentGetReq p);

    // 이 과정에서 반환되는건 무조건 영향받은 행값(0 or 1)
    int delFeedComment(FeedCommentDelReq p);
}
