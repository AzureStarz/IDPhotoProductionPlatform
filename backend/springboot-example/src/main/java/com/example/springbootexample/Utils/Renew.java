package com.example.springbootexample.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

import org.apache.commons.codec.binary.Base64;

public class Renew {
    // 对字节数组字符串进行Base64解码并生成图片
    //imgFilePath 待保存的本地路径
    public static boolean GenerateImage(String base64Str, String imgFilePath) {
        if (base64Str == null) // 图像数据为空
            return false;
        Base64 decoder = new Base64();
        try {
            FileOutputStream write = new FileOutputStream(new File(imgFilePath));
            byte[] decoderBytes = decoder.decode(base64Str);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
