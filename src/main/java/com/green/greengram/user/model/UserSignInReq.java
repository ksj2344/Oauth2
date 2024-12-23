package com.green.greengram.user.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="로그인")
public class UserSignInReq {
    @Size(min=3,max=30,message = "아이디는 3~30자로 입력해주세요")
    @Schema(title="ID", example = "mic", requiredMode=Schema.RequiredMode.REQUIRED)
    private String uid;

    @Size(min=4,max=50,message = "비밀번호는 4~50자로 입력해주세요")
    @NotNull(message = "비밀번호를 입력하셔야합니다.")
    @Schema(title="PW", example = "1212", requiredMode=Schema.RequiredMode.REQUIRED)
    private String upw;
}
