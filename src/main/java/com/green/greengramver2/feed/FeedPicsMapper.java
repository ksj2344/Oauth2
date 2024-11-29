package com.green.greengramver2.feed;

import com.green.greengramver2.feed.model.FeedPicDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicsMapper {
    int insPeedPics(FeedPicDto p);
    int insPeedPics2(FeedPicDto p);

    List<String> selFeedPicList(long feedId);
}
