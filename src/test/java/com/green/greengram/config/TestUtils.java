package com.green.greengram.config;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {
    //파라미터 dateTime으로 넘어오는 값이 DB에 저장된 dataTime값 2024-12-30 11:32:23
    public static void assertCurrentTimeStamp(String dateTime){
        //자바에서 현재 일시 데이터를 가져오도로록 한다.
        LocalDateTime expectedNow = LocalDateTime.now(); //2024-12-30T11:32:23 와 같은 타입으로 나옴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //그래서 T를 뺀 형식으로 passing 처리
        //HH: 24시간 기준 시간. hh: 12시간 기준
        LocalDateTime actualNow = LocalDateTime.parse(dateTime, formatter);

        assertTrue(Duration.between(expectedNow, actualNow).getSeconds()<=1);
    }
}
