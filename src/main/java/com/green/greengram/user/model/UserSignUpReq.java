package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserSignUpReq {
    @Schema(description = "유저 ID", example = "greeduser01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(description = "유저 pw", example = "dd11",requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;
    @Schema(description = "유저 닉네임", example = "홍길동")
    private String nickName;
}
