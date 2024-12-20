package com.green.greengram.user.follow;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.user.follow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFollowService {
    private final UserFollowMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int follow(UserFollowReq p){
        p.setFromUserId(authenticationFacade.getSignedUserId());
        return mapper.follow(p);
    }
    public int unfollow(UserFollowReq p){
        p.setFromUserId(authenticationFacade.getSignedUserId());
        return mapper.unFollow(p);
    }
}
