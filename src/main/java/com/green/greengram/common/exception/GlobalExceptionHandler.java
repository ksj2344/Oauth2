package com.green.greengram.common.exception;

import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice //AOP(Aspect Orientation Programming, 관점 지향 프로그래밍)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //(추가메소드) CustomException이 발생되었을 경우 catch
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handelException(CustomException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    //Validation 예외가 발생하였을 경우 catch
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
                                                                  , HttpHeaders headers
                                                                  , HttpStatusCode statusCode
                                                                  , WebRequest request) {
        return handelExceptionInternal(CommonErrorCode.INVALID_PARAMETER, ex);
    }

    @ExceptionHandler({MalformedJwtException.class, SignatureException.class}) //토큰 값이 유효하지 않을 때
    public ResponseEntity<Object> handleMalformedJwtException() {
        return handleExceptionInternal(UserErrorCode.INVALID_TOKEN);
    }

    @ExceptionHandler(ExpiredJwtException.class) //토큰이 만료가 되었을 때
    public ResponseEntity<Object> handleExpiredJwtException() {
        return handleExceptionInternal(UserErrorCode.EXPIRED_TOKEN);
    }


    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode){
        return handelExceptionInternal(errorCode,null);
    }

    private ResponseEntity<Object> handelExceptionInternal(ErrorCode errorCode,BindException e){
        return ResponseEntity.status(errorCode.getHttpStatus())
                            .body(makeErrorResponse(errorCode, e));
    }

    private MyErrorResponse makeErrorResponse(ErrorCode errorCode, BindException e) {
        return MyErrorResponse.builder()
                .resultMessage(errorCode.getMessage())
                .resultData(errorCode.name())
                .valids(e==null?null:getValidationErrors(e))
                .build();
    }

    //Validation 예외가 발생시 예외처리하기 위한 메소드
    private List<MyErrorResponse.ValidationError> getValidationErrors(BindException e) {
        List<FieldError> fieldErrors=e.getBindingResult().getFieldErrors();
        //List<FieldError> fieldErrors=e.getFieldErrors();
        // 에러가 없다면 아래 list의 size는 0
        List<MyErrorResponse.ValidationError> errors = new ArrayList<>(fieldErrors.size());
        for(FieldError fieldError:fieldErrors){
            errors.add(MyErrorResponse.ValidationError.of(fieldError));
        }
        return errors;
    }
}
