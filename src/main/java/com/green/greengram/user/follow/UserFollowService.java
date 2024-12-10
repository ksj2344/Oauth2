package com.green.greengram.user.follow;

import com.green.greengram.user.follow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFollowService {
    private final UserFollowMapper mapper;

    public int follow(UserFollowReq p){
        return mapper.follow(p);
    }
    public int unfollow(UserFollowReq p){
        return mapper.unFollow(p);
    }
}
