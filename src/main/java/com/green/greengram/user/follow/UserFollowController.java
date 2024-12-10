package com.green.greengram.user.follow;


import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.user.follow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user/follow")
@RequiredArgsConstructor
public class UserFollowController {
    private final UserFollowService service;

    //팔로우신청
    //requestBody, 요청을 보내는 자가 body에 JSON형태의 데이터를 보낸다.
    @PostMapping
    public ResultResponse<Integer> postUserFollow(@RequestBody UserFollowReq p){
        log.info("UserFollowController>postUserFollow>p:{}",p);
        return ResultResponse.<Integer>builder()
                .resultMessage("Follow")
                .resultData(service.follow(p))
                .build();
    }

    //팔로우 취소
    //QuerySting
    @DeleteMapping
    public ResultResponse<Integer> deleteUserFollow(@ParameterObject UserFollowReq p){
        log.info("UserFollowController>deleteUserFollow>p:{}",p);
        return ResultResponse.<Integer>builder()
                .resultMessage("UnFollow")
                .resultData(service.unfollow(p))
                .build();
    }
}
