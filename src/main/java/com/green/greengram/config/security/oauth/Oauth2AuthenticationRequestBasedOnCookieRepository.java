package com.green.greengram.config.security.oauth;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.GlobalOauth2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationRequestBasedOnCookieRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final CookieUtils cookieUtils;
    private final GlobalOauth2 globalOauth2;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return cookieUtils.getValue(request
                , globalOauth2.getAuthorizationRequestCookieName()
                , OAuth2AuthorizationRequest.class);
    }

    // 맨처음 saveAuthorizationRequest 실행하여 cookie저장
    // 저장된 쿠키 사용할 때 loadAuthorizationRequest 실행
    //끝날때 saveAuthorizationRequest 실행하여 authorizationRequest에서 null인것을 감지. removeAuthorizationRequest 하여 우리쪽 cookie도 삭제.

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null) {
            this.removeAuthorizationRequest(response);
        }
        cookieUtils.setCookie(response
                ,globalOauth2.getAuthorizationRequestCookieName()
                ,authorizationRequest
                ,globalOauth2.getCookieExpirySecond()
                ,"/"); // 일단 모든 경로에 사용
        // FE에서 요청한 redirect_uri 쿠키에 저장한다.
        String redirectUriAfterLogin = request.getParameter(globalOauth2.getRedirectUriParamCookieName());
        cookieUtils.setCookie(response
                ,globalOauth2.getRedirectUriParamCookieName()
                ,redirectUriAfterLogin
                ,globalOauth2.getCookieExpirySecond()
                ,"/");
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    private void removeAuthorizationRequest(HttpServletResponse response) {
        cookieUtils.deleteCookie(response, globalOauth2.getAuthorizationRequestCookieName());
        cookieUtils.deleteCookie(response, globalOauth2.getRedirectUriParamCookieName());
    }
}
