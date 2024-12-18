package com.green.greengram.config.security;
//Spring Security 세팅

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration //메소드 빈등록, 싱글톤으로 사용된다.
@RequiredArgsConstructor
public class WebSecurityConfig {
    //스프링 시큐리티 일부 비활성화
    //이 경로로 요청이 오는것은 security가 관여하지 않는다. (그러나 좋은 방법이 아님)
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }

    //서버사이드 랜더링 안한다면 꼭 설정해두는게 조음
    @Bean //스프링이 메소드 호출을하고 리턴한 객체 주소값을 관리한다. (빈등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //시큐리티가 세션을 사용하지 않는다.
                .httpBasic(h -> h.disable()) //SSR(sever side rendering)이 아니다. 화면을 만들지 않을 것이므로 비활성화 한다. 시큐리티 로그인창은 나타나지 않을것이다.
                .formLogin(form -> form.disable()) //SSR(sever side rendering)이 아니다. 폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) //보안관련 SSR이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                .authorizeHttpRequests(req -> req.requestMatchers("/api/feed", "/api/feed/**").authenticated() //.authenticated(): 로그인이 되어있어야만 사용가능
                        .requestMatchers(HttpMethod.GET,"/api/user").authenticated() //get방식으로 들어오는것 모두 막는다.
                        .requestMatchers(HttpMethod.PATCH,"/api/user/pic").authenticated() //patch방식으로 들어오는것 모두 막는다.
                        .anyRequest().permitAll()) //나머지 요청은 허용
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
