package com.green.greengram.user.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="로그인")
public class UserSignInReq {
    @Schema(title="ID", example = "mic", requiredMode=Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(title="PW", example = "1212", requiredMode=Schema.RequiredMode.REQUIRED)
    private String upw;
}
