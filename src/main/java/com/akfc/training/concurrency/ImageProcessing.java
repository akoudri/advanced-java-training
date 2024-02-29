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
            image = new int[height][width][3];
            int px, red, green, blue;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
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
        int numWorkers = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(numWorkers);
        chunkSize = (int) Math.ceil((double)height / numWorkers);
        Future<int[][][]>[] futures = new Future[numWorkers];
        int[][] convolution;
        double factor = 1.0;
        // Gaussian Blur
        //convolution = new int[][] { {1, 1, 1}, {1, 1, 1}, {1, 1, 1} };
        //factor = 1.0/9;
        // Détection de bords
        //convolution = new int[][] { {-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1} };
        // Repoussage
        convolution = new int[][] { {0, -1, 0}, {-1, 5, -1}, {0, -1, 0} };
        for (int i = 0; i < numWorkers; i++) {
            int start = Math.min(i * chunkSize, height);
            int end = Math.min((i + 1)*chunkSize, height);
            futures[i] = pool.submit(new TransformImage(i, start, end, convolution, factor));
        }
        try {
            for (int i = 0; i < numWorkers; i++) {
                int[][][] partial = futures[i].get();
                for (int j = 0; j < partial.length; j++) {
                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < 3; l++) {
                            tImage[j + i*chunkSize][k][l] = partial[j][k][l];
                        }
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        pool.shutdown();
    }

    public void saveImage(String name) {
        File f = new File("src/main/resources/" + name + ".jpg");
        BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int px, red, green, blue;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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

        private int num, start, end;
        double factor;
        private int[][] convolution;

        public TransformImage(int num, int start, int end, int[][] convolution, double factor) {
            this.num = num;
            this.start = start;
            this.end = end;
            this.convolution = convolution;
            this.factor = factor;
        }

        @Override
        public int[][][] call() throws Exception {
            int[][][] res = new int[end - start][width][3];
            for (int i = 0; i < end - start; i++) {
                for (int j = 0; j < width; j++) {
                    for (int k = 0; k < 3; k++) {
                        res[i][j][k] = compute(i + num*chunkSize, j, k);
                    }
                }
            }
            return res;
        }

        private int compute(int i, int j, int k) {
            int left, right, top, bottom;
            top = (i == 0)? height - 1:i - 1;
            bottom = (i == height - 1)? 0:i + 1;
            left = (j == 0)? width - 1:j - 1;
            right = (j == width - 1)? 0:j + 1;
            return (int) ((convolution[1][1] * image[i][j][k] +
                                convolution[1][0] * image[i][left][k] +
                                convolution[0][0] * image[top][left][k] +
                                convolution[0][1] * image[top][j][k] +
                                convolution[0][2] * image[top][right][k] +
                                convolution[1][2] * image[i][right][k] +
                                convolution[2][2] * image[bottom][right][k] +
                                convolution[2][1] * image[bottom][j][k] +
                                convolution[2][0] * image[bottom][left][k]) * factor);
        }
    }

    public static void main(String[] args) {
        ImageProcessing l = new ImageProcessing();
        l.transform();
        l.saveImage("Lenna2");
    }

}
