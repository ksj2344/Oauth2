package com.green.greengram.common.exception;


public interface ErrorCode {
    String name(); //enum에서 자동으로 만들어짐
    String getMessage(); //상속받은 enum은 String message 멤버필드를 꼭 가져야한다.
}
