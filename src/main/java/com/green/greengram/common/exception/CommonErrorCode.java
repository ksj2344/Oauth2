package com.green.greengram.common.exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//Enum: 객체화 된 상수. 잘못된 값을 넣지 않기 위한 장치
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("서버 내부에서 에러가 발생하였습니다.")
    , INVALID_PARAMETER("잘못된 파라미터입니다.")
    ; //에러코드가 더 필요하면 계속 추가하면 됨
    private final String message;
}
