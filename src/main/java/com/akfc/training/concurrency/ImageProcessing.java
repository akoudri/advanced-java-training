package com.akfc.training.concurrency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;

public class ImageProcessing {

    private int[][][] image, tImage;
    private int width, height, chunkSize;

    public ImageProcessing() {
        loadImageFromURL("https://bellard.org/bpg/lena30.jpg");
    }

    private void loadImageFromURL(String url) {
        try {
            URL photoUrl = new URL(url);
            BufferedInputStream in = new BufferedInputStream(photoUrl.openStream());
            BufferedImage buff = ImageIO.read(in);
            width = buff.getWidth();
            height = buff.getHeight();
            image = new int[width][height][3];
            int px, red, green, blue;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    px = buff.getRGB(i, j);
                    red = (px >> 16) & 0xFF;
                    green = (px >> 8) & 0xFF;
                    blue = px & 0xFF;
                    image[i][j][0] = red;
                    image[i][j][1] = green;
                    image[i][j][2] = blue;
                }
            }
            tImage = image.clone();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void transform() {
        //TODO
    }

    public void saveImage(String name) {
        File f = new File("resources/" + name + ".jpg");
        BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int px, red, green, blue;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                red = tImage[i][j][0];
                green = tImage[i][j][1];
                blue = tImage[i][j][2];
                px = (red << 16) | (green << 8) | blue;
                buff.setRGB(i, j, px);
            }
        }
        try {
            ImageIO.write(buff, "jpg", f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class TransformImage implements Callable<int[][][]> {

        @Override
        public int[][][] call() throws Exception {
            int[][][] res = null;
            //TODO: compute res
            return res;
        }
    }

    public static void main(String[] args) {
        ImageProcessing l = new ImageProcessing();
        l.transform();
        l.saveImage("Lenna2");
    }

}
