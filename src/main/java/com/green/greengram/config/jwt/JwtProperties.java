package com.green.greengram.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.beans.ConstructorProperties;

@Getter
@Setter
@ConfigurationProperties("jwt")  //yml에 있는 jwt를 setter를 통해 담겠다.  //겸사겸사 빈등록도
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
