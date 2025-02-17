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

    public int postUserFollow(UserFollowReq p){
        p.setFromUserId(authenticationFacade.getSignedUserId());
        return mapper.insUserFollow(p);
    }
    public int deleteUserFollow(UserFollowReq p){
        p.setFromUserId(authenticationFacade.getSignedUserId());
        return mapper.delUserFollow(p);
    }
}
