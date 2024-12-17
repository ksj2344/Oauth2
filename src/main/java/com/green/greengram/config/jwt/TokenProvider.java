package com.green.greengram.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
public class TokenProvider {
    private final ObjectMapper objectMapper; //JackSon 라이브러리
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public TokenProvider(ObjectMapper objectMapper, JwtProperties jwtProperties) {
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
        this.secretKey=Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
                //hmac: 대칭키, sha: 암호화 방법  //encode: 암호화, decode: 복호화
    }

    // JWT 생성
    public String generateToken(JwtUser jwtUser, Duration expiredAt) { //jwtUser: 인증정보, expiredAt: 기간
        Date now = new Date(); //시간측정
        return makeToken(jwtUser, new Date(now.getTime()+expiredAt.toMillis()));
    }

    //JWT 생성
    private String makeToken(JwtUser jwtUser, Date expiry) {
        return Jwts.builder()
                .header().add("typ","JWT")
                         .add("alg","HS256")
                .and()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(expiry)
                .claim("signedUser", makeClaimByUserToString(jwtUser))
                .signWith(secretKey)
                .compact();  //원래 빌더패턴은 build()로 담는데, 마지막에 compact()쓴 이유은ㄴ 이걸 ㅏㅇㅇ모
    }

    //JWT 암호화
    private String makeClaimByUserToString(JwtUser jwtUser) { //객체를 String으로 바꾸는 작업: 직렬화
        //객체 자체를 JWT에 담고 싶어서 객체를 직렬화
        //jwtUser에 담고있는 데이터를 JSON 형태의 문자열로 변환
        //여기서 objectMapper는 jackson
        try {
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //JWT 복호화
    public boolean validToken(String token) {
        try{
            getClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //Authentication: Security의 미들웨어. 부품같은거라고보면됨. spring framework security로 import할것.
    //authorities: 권한
    // List, Set의 차이: 둘다 Collections인데 Set은 중복 제거가 됨.
    public Authentication getAuthentication(String token) {
        UserDetails userDetails=getUserDetailsFromToken(token);
        return userDetails==null?null:new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public UserDetails getUserDetailsFromToken(String token) {
        Claims claims=getClaims(token);
        String json=(String)claims.get("signedUser");
        JwtUser jwtUser=objectMapper.convertValue(json,JwtUser.class);
        MyUserDetails userDetails=new MyUserDetails();
        userDetails.setJwtUser(jwtUser);
        return userDetails;
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
