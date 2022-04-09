package com.example.springbootexample.Utils;

import com.mysql.cj.protocol.MessageListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageResizer {

    public static byte[] compressImage(byte[] imageByte, double scale) {
        byte[] smallImage = null;
        int width = 0, height = 0;

        if (imageByte == null)
            return null;

        ByteArrayInputStream byteInput = new ByteArrayInputStream(imageByte);
        try {
            Image image = ImageIO.read(byteInput);
            int w = image.getWidth(null);
            int h = image.getHeight(null);
            // adjust weight and height to avoid image distortion

            width = (int) (w * scale);
            width -= width % 4;
            height = (int) (h * scale);

            if (scale >= (double) 1)
                return imageByte;

            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            buffImg.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", out);
            smallImage = out.toByteArray();
            return smallImage;

        } catch (IOException e) {
            return null;
        }
    }

}