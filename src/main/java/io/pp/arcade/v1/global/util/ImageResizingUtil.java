package io.pp.arcade.v1.global.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageResizingUtil {
    public static byte[] resizeImageBytes(byte[] downloadedImageBytes, double ratio) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(downloadedImageBytes);
        BufferedImage image = resize(ImageIO.read(inputStream), ratio);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        return baos.toByteArray();
    }

    private static BufferedImage resize(BufferedImage img, double ratio) {
        int newWidth = (int) (img.getWidth() * ratio);
        int newHeight = (int) (img.getHeight() * ratio);
        System.out.println("newHeight +  = " + newHeight + " newWidth = " + newWidth);
        Image imageToResize = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(imageToResize, 0, 0, newWidth, newHeight, null);
        return resizedImage;
    }
}
