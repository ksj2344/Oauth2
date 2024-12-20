package com.green.greengram.feed.like;


import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int feedLikeToggle(FeedLikeReq p){
        p.setUserId(authenticationFacade.getSignedUserId());
        int result = mapper.delFeedLike(p); //영향받은 행으로 처리가 가능함
        if(result == 0){
            return mapper.insFeedLike(p); //좋아요 처리시 return 1
        }
        return 0; //좋아요 취소 시 return 0
    }
}
