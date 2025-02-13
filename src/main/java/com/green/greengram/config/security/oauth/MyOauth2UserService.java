package com.green.greengram.config.security.oauth;

import com.green.greengram.config.security.SignInProviderType;
import com.green.greengram.config.security.oauth.userInfo.Oauth2UserInfo;
import com.green.greengram.config.security.oauth.userInfo.Oauth2UserInfoFactory;
import com.green.greengram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository; //데이터베이스 통신을 위함. mybatis쓸거면 mapper가져와야함
    private final Oauth2UserInfoFactory oauth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        return null;
    }

    private OAuth2User process(OAuth2UserRequest req){
        OAuth2User oAuth2User = super.loadUser(req); //DefaultOAuth2UserService의 메소드를 가공하여 쓰기 위함. 이러고 loadUser에 넘길것임

        /*
            req.getClientRegistration().getRegistrationId().toUpperCase() : 소셜 로그인 신청한 플랫폼 문자열 값이 넘어온다
            플랫폼 문자열 값은 yml에서 설정한
            spring.security.oauth2.client.registration 아래에 있는 속성값들이다. (google, kakao, naver)
         */
        SignInProviderType signInProviderType = SignInProviderType.valueOf(req.getClientRegistration()
                                                                    .getRegistrationId()
                                                                     .toUpperCase());
        //사용하기 편하도록 규격화 된 객체로 변환
        Oauth2UserInfo oauth2UserInfo=oauth2UserInfoFactory.getOauth2UserInfo(signInProviderType, oAuth2User.getAttributes());

        //기존에 회원가입이 되어있는지 체크
    }
}
