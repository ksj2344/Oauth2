package com.green.greengram.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="피드 등록 요청")
public class FeedPostReq {
    @Schema(title="로그인 유저 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long writerUserId;
    @Schema(title="피드 내용", example = "피드내용 테스트")
    private String contents;
    @Schema(title="피드 위치", example = "그린컴퓨터학원")
    private String location;
    @JsonIgnore
    private long feedId;
}
