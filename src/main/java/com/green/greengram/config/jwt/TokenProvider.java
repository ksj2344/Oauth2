package com.green.greengram.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

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
        Date now = new Date(); //시간측정 //일시정보가 있는 DATE 객체
        return makeToken(jwtUser, new Date(now.getTime()+expiredAt.toMillis()));
        //now.getTime(): 현재시간 long타입 값, expiredAt.toMillis(): 현재 밀리컨드 long타입 값, 합치면 만료 일시(expiry)
    }

    //JWT 생성
    private String makeToken(JwtUser jwtUser, Date expiry) {
        JwtBuilder builder = Jwts.builder();
        JwtBuilder.BuilderHeader header = builder.header();
        header.type("JWT");

        builder.issuer(jwtProperties.getIssuer());

        return Jwts.builder()
                .header().type("JWT") //JWT 타입으로 온다는 설명
                .and()
                .issuer(jwtProperties.getIssuer()) //yml에 있는 green@green.kr
                .issuedAt(new Date())
                .expiration(expiry)
                .claim("signedUser", makeClaimByUserToString(jwtUser)) //claim에 signedUser key로 json형태의 문자열 value가 담김
                .signWith(secretKey)
                .compact();  //원래 빌더패턴은 build()로 담는데, 마지막에 compact()쓴 이유은 jason직렬화 하려고
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
        }catch (Exception e){
            return false;
        }
        return true;
    }

    //Spring Security에서 인증처리 하기. Authentication 객체가 필요함.
    //Authentication: Security의 미들웨어. 부품같은거라고보면됨. spring framework security로 import할것.
    //authorities: 권한
    // List, Set의 차이: 둘다 Collections인데 Set은 중복 제거가 됨.
    public Authentication getAuthentication(String token) {
        UserDetails userDetails=getUserDetailsFromToken(token);
        return userDetails==null
                ?null
                :new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                //Authentication을 구현화한 객체의 생성자 호출,
                                //userDetails : 사용자 정보(Principal)
                                // userDetails.getAuthorities(): 사용자 권한
    }

    public JwtUser getJwtUserFromToken(String token) {
        Claims claims=getClaims(token);
        String json=(String)claims.get("signedUser");
        JwtUser jwtUser= null;
        try {
            jwtUser = objectMapper.readValue(json, JwtUser.class); //readValue(): JSON을 역직렬화
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jwtUser;
    }

    public UserDetails getUserDetailsFromToken(String token) {
        JwtUser jwtUser= getJwtUserFromToken(token);
        MyUserDetails userDetails=new MyUserDetails();
        userDetails.setJwtUser(jwtUser);
        return userDetails;
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
