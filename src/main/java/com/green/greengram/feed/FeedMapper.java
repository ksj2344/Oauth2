package com.green.greengram.feed;

import com.green.greengram.feed.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedPostReq p);
    List<FeedGetRes> selFeedList(FeedGetReq p);
    List<FeedAndPicDto> selFeedWithPicList(FeedGetReq p); //pic도 같이 가져오는경우(M+1해결)
    List<FeedWithPicCommentDto> selFeedWithPicAndCommentLimit4List(FeedGetReq p);

    int delFeedLikeAndFeedCommentAndFeedPic(FeedDeleteReq p);
    int delFeed(FeedDeleteReq p);
}
