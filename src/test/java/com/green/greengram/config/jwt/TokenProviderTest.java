package com.green.greengram.config.jwt;

import com.green.greengram.config.security.MyUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {
    //테스트는 생성자를 이용한 DI가 불가능
    //DI방법은 필드, setter메소드, 생성자
    //테스트 때는 필드 주입방식을 활용함.

    @Autowired//리플랙션 API를 이용해서 setter가 없어도 가능
    private TokenProvider tokenProvider;

    @Test
    public void generateToken() {
        //Given(준비단계)
        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        jwtUser.setRoles(roles);

        //When(실행단계)
        String token= tokenProvider.generateToken(jwtUser, Duration.ofHours(3));

        //Then(검증단계)
        assertNotNull(token);

        System.out.println("token: "+token);
    }

    @Test
    void validToken(){
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMTQ1OCwiZXhwIjoxNzM0NDAxNTE4LCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.E9Bbpu1HiePDyixxF30tTMNS1ZUVPv2GfvlY7vNPOyT5CA73OoPRy_9ywS9_QcE6b2gwLYXCs5it4ERTakTyTg";
        //boolean result=tokenProvider.validToken(token);
        //@assertFalse(result);
    }

    @Test
    void getAuthentication(){
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMzc1NCwiZXhwIjoxNzM0NDE0NTU0LCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.YoWEj9GzmVcfoLoauqAbedqRkmo5VuM6PZvKUjlIxBDsWUfQvAwYewzWHL4ZassktNnSAJJRW5OB1GUWCdSIoA";//3h 짜리
        Authentication authentication=tokenProvider.getAuthentication(token);
        assertNotNull(authentication);

        MyUserDetails myUserDetails=(MyUserDetails) authentication.getPrincipal(); //(MyUserDetails) 형변환. 거기있는 getter쓰려고
        JwtUser jwtUser=myUserDetails.getJwtUser();

        JwtUser expectedJwtUser=new JwtUser();
        expectedJwtUser.setSignedUserId(10);

        List<String> roles=new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        expectedJwtUser.setRoles(roles);

        assertEquals(expectedJwtUser,jwtUser);
    }
}