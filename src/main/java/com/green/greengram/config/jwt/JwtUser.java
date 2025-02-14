package com.green.greengram.config.jwt;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode //서로 같은 값을 가졌으면 true로 반환하는 오버라이딩 에노테이션
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser {
    private long signedUserId;
    private List<String> roles; //인가(권한)처리 때 사용, ROLE_이름. ROLE_USER, ROLE_ADMIN 같은 식으로 작성
}
