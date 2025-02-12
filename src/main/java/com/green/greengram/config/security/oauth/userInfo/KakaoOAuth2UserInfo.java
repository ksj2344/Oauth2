package com.green.greengram.config.security.oauth.userInfo;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Map;
/*
    kakao 유저정보 response JSON
    {
      "id" : "12122",
      "account_email" : "ddd@daum.net",
      "properties" : {
        "nickname" : "홍길동",
        "thumbnail_image" : "profile.jpg"
      }
    }
 */

public class KakaoOAuth2UserInfo extends Oauth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String,Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties == null? "" : properties.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        String email= (String)attributes.get("account_email").toString();
        return email == null? "" : email;
    }

    @Override
    public String getProfileImageUrl() {
        Map<String,Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties == null? "" : properties.get("thumbnail_image").toString();
    }
}
