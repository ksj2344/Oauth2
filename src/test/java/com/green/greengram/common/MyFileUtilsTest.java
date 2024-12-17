package com.green.greengram.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyFileUtilsTest {
    MyFileUtils myFileUtils;

    @BeforeEach
    void setUp() {
        myFileUtils=new MyFileUtils("D:/ksj/download/greengram_ver3");
    }

    @Test
    void deleteFolder() {
        String path=String.format("%s/user/ddd",myFileUtils.getUploadPath());
        myFileUtils.deleteFolder(path,false);
    }
}