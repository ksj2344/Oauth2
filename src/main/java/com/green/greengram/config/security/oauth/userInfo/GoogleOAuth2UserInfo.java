package com.green.greengram.config.security.oauth.userInfo;

import java.util.Map;


//근데 구글은 로컬에서만 허용함
public class GoogleOAuth2UserInfo extends Oauth2UserInfo{


    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("picture").toString();
    }
}
