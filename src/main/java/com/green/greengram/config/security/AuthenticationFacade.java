package com.green.greengram.config.security;

import com.green.greengram.config.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//Authentication에 담아놓은 값 호출하려고 사용
@Component
public class AuthenticationFacade {
    public JwtUser getSignedUser(){
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext()
                                                                            .getAuthentication()
                                                                            .getPrincipal();
        return myUserDetails.getJwtUser();
    }

    public long getSignedUserId(){
        return getSignedUser().getSignedUserId();
    }
}
