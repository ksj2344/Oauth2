package com.green.greengram.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Schema(title="유저정보 GET 응답")
public class UserInfoGetRes {
    private long userId;
    private String pic;
    private String createdAt;
    private String nickName;
    private int follower;
    private int following;
    @Schema(description="프로필 유저가 작성한 피드의 수")
    private int feedCount;
    @Schema(description="프로필 유저가 피드로 받은 좋아요 수 총합")
    private int myFeedLikeCount;
    private int followState;
}
