package com.green.greengramver2.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="피드 댓글 등록 요청") //GET방식때는 description
public class FeedCommentPostReq {
    @Schema(title = "피드PK",example ="1" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;  //누가
    @Schema(title = "로그인한 유저PK",example ="2" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;  //어느피드에
    @Schema(title = "댓글 내용",example ="퍼가요~" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;  //어떤 내용을 담았다.

    @JsonIgnore
    private long feedCommentId;
}
