package com.green.greengram.feed.like;

import com.green.greengram.feed.like.model.FeedLikeReq;
import com.green.greengram.feed.like.model.FeedLikeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedLikeTestMapper {
    @Select("SELECT * FROM feed_like WHERE feed_id = #{feedId} AND user_id = #{userId}") //null 아니면 튜플 하나 넘어옴
    FeedLikeVo selFeedLikeByFeedIdAndUserId(FeedLikeReq p);
    @Select("SELECT * FROM feed_like") //List로 반환할 때 해당하는것이 하나도 없다면 0 size list가 온다
    List<FeedLikeVo> selFeedLikeAll();
}
