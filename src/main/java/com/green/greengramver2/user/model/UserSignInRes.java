package com.green.greengramver2.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="로그인 응답")
public class UserSignInRes {
    private long userId;
    private String nickName;
    private String pic;
    @JsonIgnore//swagger 표시 안되지만, 응답 시 빼는 역할도 한다.
    private String upw; //ID와 PW를 동시검증할 수 없기 때문에 PW를 java단으로 가져와서 틀렸는지 검증한다.
    @JsonIgnore
    private String message;
}
