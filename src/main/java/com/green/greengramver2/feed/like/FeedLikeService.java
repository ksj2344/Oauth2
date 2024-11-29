package com.green.greengramver2.feed.like;


import com.green.greengramver2.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeMapper mapper;

    //좋아요 등록시 return 1, 좋아요 취소시 return 0
    public int feedLikeToggle(FeedLikeReq p){
        int result = mapper.delFeedLike(p);
        if(result == 0){
            return mapper.insFeedLike(p);
        }
        return 0;
    }
}
