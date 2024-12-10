package com.green.greengram.user.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Setter
@ToString
public class UserFollowReq {  //쿼리스트링때는 title이 안보이니 쿼리스트링 쓸거면 description 쓸것
    @JsonProperty("from_user_id") //JSON이나 쿼리스트링이나 둘다 같은 DTO 쓰려고 설정함
    @Schema(name="from_user_id",description="팔로워 유저pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long fromUserId;
    @JsonProperty("to_user_id")
    @Schema(name="to_user_id",description="팔로잉 유저pk", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long toUserId;

    @ConstructorProperties({"from_user_id","to_user_id"})
    public UserFollowReq(long fromUserId, long toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
}
