package com.green.greengram.config.jwt;
//필터(요청이 올 때마다 실행됨)
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter 필터의 타입.
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION= "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //HttpServletRequest: 쿼리스트링과 ip를 비롯한 모든 request정보가 여기 담김.
        log.info("ip address:{}",request.getRemoteAddr());
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);//Bearer 토큰값
        log.info("authorizationHeader:{}",authorizationHeader);

        String token = getAccessToken(authorizationHeader); //토큰 얻어오기
        log.info("token:{}",token);

        if(tokenProvider.validToken(token)) { //정상인지 확인
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth); //SecurityContextHolder에 Authentication 적용
        }


        filterChain.doFilter(request, response);
    }

    public String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) { //.startsWith(문장): 문장 으로 시작하느냐?
            return authorizationHeader.substring(TOKEN_PREFIX.length()); //Bearer 이후의 토큰값을 잘라서 리턴한다.
        }
        return null;
    }
}
