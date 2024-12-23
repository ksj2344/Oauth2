package com.green.greengram.common.exception;

import org.springframework.http.HttpStatus;

//commonErrorCode와 UserErrorCode도 이 인터페이스를 상속받았기 때문에 저 둘의 에러코드를 쓸 수있음
public interface ErrorCode {
    String name(); //enum에서 자동으로 만들어짐
    String getMessage(); //상속받은 enum은 String message 멤버필드를 꼭 가져야한다.
    HttpStatus getHttpStatus(); //응답코드 결정.  //accessToken 만료시 401 코드로 반환되게 함.
}
