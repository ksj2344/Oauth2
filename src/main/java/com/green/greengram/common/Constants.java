package com.green.greengram.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component //빈등록
public class Constants {
    private static int default_page_size;

    @Value("${const.default-page-size}")
    public void setDefaultpagesize(int value) {
        default_page_size = value;
    }

    public static int getDefaultPageSize() {
        return default_page_size;
    }
}