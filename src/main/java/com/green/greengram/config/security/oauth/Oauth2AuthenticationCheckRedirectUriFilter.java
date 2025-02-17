package com.green.greengram.config.security.oauth;

import com.green.greengram.common.GlobalOauth2;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationCheckRedirectUriFilter extends OncePerRequestFilter {
    private final GlobalOauth2 globalOauth2;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
            호스트 주소값을 제외한 요청한 URI
            예 ) http://localhost:8080/oauth2/authorization
            호스트 주소값 : http://localhost:8080
            제외한 요청 URI(requestUri): /oauth2/authorization
         */
        String requestUri = request.getRequestURI(); //서버에 요청을 보내는 주소값
        log.info("request uri: {}", requestUri);
        if(requestUri.startsWith(globalOauth2.getBaseUri())) { //뒤에 쿼리스트링으로 들어오므로 해당 uri로 시작한다면 소셜로그인으로 취급.
            //소셜 로그인이 맞다면 redirect값과 맞는지 확인한다.
            String redirectUri= request.getParameter("redirect_uri"); //쿼리스트링이나 body에 있는 정보를 받아옴. 둘다 있으면 쿼리스트링 먼저 받아옴.
            // 주소 ? redirect_uri=abc 라면 abc를 받아올 것.
            if(redirectUri != null && !hasAuthorizedRedirectUri(redirectUri)) { //약속한 redirect_uri값이 아니었다면
                String errRedirectUrl= UriComponentsBuilder.fromUriString("/err_message")
                        .queryParam("message", "유효한 Redirect URL이 아닙니다.").encode()
                        .toUriString();
                //errRedirectUrl = "/err_message? message = 유효한 Redirect URL이 아닙니다.
                //encode() : 복호화
                response.sendRedirect(errRedirectUrl);
                return;
            }

        }

        filterChain.doFilter(request, response); //문제가 없다면 다음 필터로
    }
    //FE 쪽에서 <a helf= 이런식으로 시작하면 무조건 get 방식으로 보면됨.

    //약속한 redirect_uri가 맞는지 체크, 없으면 false, 있으면 true 리턴
    private boolean hasAuthorizedRedirectUri(String redirectUri){
        for(String uri : globalOauth2.getAuthorizedRedirectUris()){
            if(uri.equals(redirectUri)){
                return true;
            }
        }
        return false;
    }

}
