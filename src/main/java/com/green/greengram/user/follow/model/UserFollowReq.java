package com.green.greengram.user.follow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@EqualsAndHashCode
@ToString
public class UserFollowReq {  //쿼리스트링때는 title이 안보이니 쿼리스트링 쓸거면 description 쓸것
    @JsonProperty("from_user_id") //JSON이나 쿼리스트링이나 둘다 같은 DTO 쓰려고 설정함
    @JsonIgnore
    private long fromUserId;
    @JsonProperty("to_user_id")
    @Schema(name="to_user_id",description="팔로잉 유저pk", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long toUserId;

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    @ConstructorProperties({"to_user_id"})
    public UserFollowReq(long toUserId) {
        this.toUserId = toUserId;
    }
}
