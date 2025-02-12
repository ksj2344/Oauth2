package com.green.greengram.config.security.oauth.userInfo;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/*
    플랫폼으로부터 받고싶은 데이터를 저장하고
    원하는 데이터를 리턴하는 규격
*/
@RequiredArgsConstructor
public abstract class Oauth2UserInfo { //추상클래스(규격)
    protected final Map<String, Object> attributes; //UserInfo JSON > Map

    public abstract String getId(); //유일값 리턴 용도로 쓸 메소드
    public abstract String getName(); //이름 리턴 용도로 쓸 메소드
    public abstract String getEmail(); //이메일 리턴 용도로 쓸 메소드
    public abstract String getProfileImageUrl(); //프로파일 사진 URL 리턴 용도로 쓸 메소드
}
