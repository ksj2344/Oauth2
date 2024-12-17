package com.green.greengram.config.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {
    //테스트는 생성자를 이용한 DI가 불가능

    @Test
    public void generateToken() {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        jwtUser.setRoles(roles);
    }
}