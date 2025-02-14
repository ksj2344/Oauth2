package com.green.greengram.config.security.oauth;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.GlobalOauth2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
//실패시 오류메세지 처리하는 핸들러
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final CookieUtils cookieUtils;
    private final GlobalOauth2 globalOauth2;

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException {
        exception.printStackTrace();
        //FE- Redirect-Url 획득 from Cookie
        String redirectUrl = cookieUtils.getValue(req, globalOauth2.getRedirectUriParamCookieName(), String.class);

        //URL에 에러 쿼리스트링 추가
        String targetUrl = redirectUrl == null ? "/" : UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build()
                .toUriString();

        //targetUrl = "http://localhost:8080/fe/redirect?error=에러메세지";
        getRedirectStrategy().sendRedirect(req, res, targetUrl);
    }

}
