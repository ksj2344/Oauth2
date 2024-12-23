package com.green.greengram.common.exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
//500단위 에러: 서버측 에러
//400단위 에러: 클라이언트측 에러
//Enum: 객체화 된 상수. 잘못된 값을 넣지 않기 위한 장치
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부에서 에러가 발생하였습니다.")
    , INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"잘못된 파라미터입니다.")
    ; //에러코드가 더 필요하면 계속 추가하면 됨

    private final HttpStatus httpStatus;
    private final String message;
}
//CommonErrorCode 타입의 값을 쓴다면 INTERNAL_SERVER_ERROR 혹은 INVALID_PARAMETER만 쓴다.
