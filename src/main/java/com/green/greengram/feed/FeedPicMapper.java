package com.green.greengram.feed;

import com.green.greengram.feed.model.FeedPicDto;
import com.green.greengram.feed.model.FeedPicSelDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicMapper {
    int insFeedPic(FeedPicDto p);
    int insFeedPic2(FeedPicDto p);

    List<String> selFeedPicList(long feedId);

    List<FeedPicSelDto> selFeedPicListByFeedIds(List<Long> feedIds);
}
