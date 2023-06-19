package com.ustlearn;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

public class UploadTest {
    @Test
    public void test(){
        String s="sejjt.jpg";
        String suffix = s.substring(s.lastIndexOf("."));
        System.out.println(suffix);

    }
}
