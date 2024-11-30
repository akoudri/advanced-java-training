package com.akfc.training.concurrency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ImageProcessing {

    private int[][][] image, tImage;
    private int width, height, chunkSize;
    private int originalWidth, originalHeight;

    public ImageProcessing() {
        loadImageFromUrl("https://bellard.org/bpg/lena30.jpg");
    }

    public void loadImageFromFile(String fileName) {
        try {
            // Adjust this if your resources folder is located differently
            File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
            BufferedImage buff = ImageIO.read(file);
            width = buff.getWidth();
            height = buff.getHeight();
            originalWidth = width;
            originalHeight = height;
            image = new int[height][width][3];
            int px, red, green, blue;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    px = buff.getRGB(j, i);
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

    public void loadImageFromBuffer(BufferedImage buff) {
        width = buff.getWidth();
        height = buff.getHeight();
        originalWidth = width;
        originalHeight = height;
        image = new int[height][width][3];
        int px, red, green, blue;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                px = buff.getRGB(j, i);
                red = (px >> 16) & 0xFF;
                green = (px >> 8) & 0xFF;
                blue = px & 0xFF;
                image[i][j][0] = red;
                image[i][j][1] = green;
                image[i][j][2] = blue;
            }
        }
        tImage = image.clone();
    }

    public void loadImageFromUrl(String url) {
        try {
            URL photoUrl = new URL(url);
            BufferedInputStream in = new BufferedInputStream(photoUrl.openStream());
            BufferedImage buff = ImageIO.read(in);
            width = buff.getWidth();
            height = buff.getHeight();
            originalWidth = width;
            originalHeight = height;
            image = new int[height][width][3];
            int px, red, green, blue;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    px = buff.getRGB(j, i);
                    red = (px >> 16);
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
        //TODO: call the transformImage method
        edgeDetection();
    }

    public void saveImage(String name) {
        File f = new File("src/main/resources/" + name + ".jpg");
        BufferedImage buff = applyTransformation();
        width = originalWidth;
        height = originalHeight;
        try {
            ImageIO.write(buff, "jpg", f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BufferedImage applyTransformation() {
        BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int px, red, green, blue;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                red = tImage[i][j][0];
                green = tImage[i][j][1];
                blue = tImage[i][j][2];
                px = (red << 16) | (green << 8) | blue;
                buff.setRGB(j, i, px);
            }
        }
        return buff;
    }

    public void keepLayer(int layer) {
        tImage = new int[height][width][3];
        int i = 0, j = 0, k = 0;
        while (i < height)
        {
            while (j < width) {
                while (k < 3) {
                    if (k == layer) tImage[i][j][k] = image[i][j][k];
                    else tImage[i][j][k] = 0;
                    k++;
                }
                k = 0;
                j++;
            }
            j = 0;
            i++;
        }
    }

    public void dropLayer(int layer) {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    if (k != layer) tImage[i][j][k] = image[i][j][k];
                    else tImage[i][j][k] = 0;
                }
            }
        }
    }

    public void flipVertically() {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tImage[i][width - j - 1] = image[i][j];
            }
        }
    }

    public void flipHorizontally() {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                tImage[height - i - 1][j] = image[i][j];
            }
        }
    }

    public void grayScale() {
        tImage = new int[height][width][3];
        int gray;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                gray = (image[i][j][0] + image[i][j][1] + image[i][j][2]) / 3;
                tImage[i][j][0] = gray;
                tImage[i][j][1] = gray;
                tImage[i][j][2] = gray;
            }
        }
    }

    public int[] averageColor() {
        int[] avg = new int[3];
        int red = 0, green = 0, blue = 0;
        int count = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width;j++){
                red += image[i][j][0];
                green += image[i][j][1];
                blue += image[i][j][2];
                count++;
            }
        }
        avg[0] = red / count;
        avg[1] = green / count;
        avg[2] = blue / count;
        return avg;
    }

    public void cropCircle() {
        tImage = new int[height][width][3];
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);
        int x, y;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                x = j - centerX;
                y = i - centerY;
                if (x * x + y * y < radius * radius) {
                    tImage[i][j] = image[i][j];
                } else {
                    tImage[i][j] = averageColor();
                }
            }
        }
    }

    public void verticalShift(double value) {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                tImage[i][j] = image[i][(j + (int)(width * value)) % width];
            }
        }
    }

    public void horizontalShift(double value) {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            tImage[i] = image[(i + (int)(height * value)) % height];
        }
    }

    public void zoomCenter() {
        tImage = new int[height][width][3];
        int ti = 0, tj = 0;
        int val;
        for (int i = height / 4; i < 3 * height / 4; i++) {
            for (int j = width / 4; j < 3 * width / 4; j++) {
                for (int k = 0; k < 3; k++) {
                    val = image[i][j][k];
                    tImage[ti][tj][k] = val;
                    tImage[ti+1][tj][k] = val;
                    tImage[ti][tj+1][k] = val;
                    tImage[ti+1][tj+1][k] = val;
                }
                tj += 2;
            }
            tj = 0;
            ti += 2;
        }
    }

    public void sepia() {
        tImage = new int[height][width][3];
        int red, green, blue;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                tImage[i][j][0] = Math.min(255, (int) (0.393 * image[i][j][0] + 0.769 * image[i][j][1] + 0.189 * image[i][j][2]));
                tImage[i][j][1] = Math.min(255, (int) (0.349 * image[i][j][0] + 0.686 * image[i][j][1] + 0.168 * image[i][j][2]));
                tImage[i][j][2] = Math.min(255, (int) (0.272 * image[i][j][0] + 0.534 * image[i][j][1] + 0.131 * image[i][j][2]));
            }
        }
    }

    public void invert() {
        tImage = new int[height][width][3];
        int red, green, blue;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                tImage[i][j][0] = 255 - image[i][j][0];
                tImage[i][j][1] = 255 - image[i][j][1];
                tImage[i][j][2] = 255 - image[i][j][2];
            }
        }
    }

    public void swap(int times) {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    tImage[i][j][k] = image[i][j][(k + times) % 3];
                }
            }
        }
    }

    public void adjustBrightness(int val) {
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    tImage[i][j][k] = Math.max(0, Math.min(255, image[i][j][k] + val));
                }
            }
        }
    }

    public void adjustContrast(int factor) {
        tImage = new int[height][width][3];
        int red, green, blue;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tImage[i][j][0] = Math.min(255, Math.max(0, (image[i][j][0] - 128) * factor + 128));
                tImage[i][j][1] = Math.min(255, Math.max(0, (image[i][j][1] - 128) * factor + 128));
                tImage[i][j][2] = Math.min(255, Math.max(0, (image[i][j][2] - 128) * factor + 128));
            }
        }
    }

    public void oldStyle(int borderWidth, int offset, int brightness) {
        tImage = new int[height][width][3];
        for (int i = borderWidth; i < height - borderWidth; i += offset) {
            for (int j = borderWidth; j < width - borderWidth; j += offset) {
                if (brightness > 0) {
                    for (int k = 0; k < 3; k++) {
                        tImage[i][j][k] = Math.min(image[i][j][k] + brightness, 255);
                    }
                } else {
                    for (int k = 0; k < 3; k++) {
                        tImage[i][j][k] = Math.max(image[i][j][k] + brightness, 0);
                    }
                }

            }
        }
    }

    public void rightRotate() {
        tImage = new int[width][height][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                for (int k = 0; k < 3; k++) {
                    tImage[j][height - i - 1][k] = image[i][j][k];
                }
            }
        }
    }

    public void leftRotate() {
        tImage = new int[width][height][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                for (int k = 0; k < 3; k++) {
                    tImage[width - j - 1][i][k] = image[i][j][k];
                }
            }
        }
    }

    public void rotate(int angle) {
        tImage = new int[width][height][3];
        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        int centerX = width / 2;
        int centerY = height / 2;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                int x = (int) (cos * (j - centerX) + sin * (i - centerY) + centerX);
                int y = (int) (cos * (i - centerY) - sin * (j - centerX) + centerY);
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    for (int k = 0; k < 3; k++) {
                        tImage[y][x][k] = image[i][j][k];
                    }
                }
            }
        }
    }

    //Simple scale - more complex are required to avoid pixelation
    //like bilinear or bicubic interpolation
    public void scale(double factor) {
        height = (int) (height * factor);
        width= (int) (width * factor);
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    tImage[i][j][k] = image[(int) (i / factor)][(int) (j / factor)][k];
                }
            }
        }
    }

    public void stretchVertically(double factor) {
        height = (int) (height * factor);
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            int y = (int) (i / factor);
            y = Math.min(y, originalHeight - 1); // Ensure we don't exceed original height
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    tImage[i][j][k] = image[y][j][k];
                }
            }
        }
    }

    public void stretchHorizontally(double factor) {
        width = (int) (width * factor);
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = (int) (j / factor);
                x = Math.min(x, originalWidth - 1); // Ensure we don't exceed original width
                for (int k = 0; k < 3; k++) {
                    tImage[i][j][k] = image[i][x][k];
                }
            }
        }
    }

    private int compute(int i, int j, int k, int[][] kernel, double factor) {
        int left, right, top, bottom;
        top = (i == 0)? height - 1:i - 1;
        bottom = (i == height - 1)? 0:i + 1;
        left = (j == 0)? width - 1:j - 1;
        right = (j == width - 1)? 0:j + 1;
        int result = (int) ((kernel[1][1] * image[i][j][k] +
                kernel[1][0] * image[i][left][k] +
                kernel[0][0] * image[top][left][k] +
                kernel[0][1] * image[top][j][k] +
                kernel[0][2] * image[top][right][k] +
                kernel[1][2] * image[i][right][k] +
                kernel[2][2] * image[bottom][right][k] +
                kernel[2][1] * image[bottom][j][k] +
                kernel[2][0] * image[bottom][left][k]) * factor);
        return Math.max(0, Math.min(result, 255));
    }

    private void convolution(int[][] kernel, double factor) {
        //We assume that kernel is 3x3 matrix
        tImage = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    tImage[i][j][k] = compute(i, j, k, kernel, factor);
                }
            }
        }
    }

    public void blur() {
        int[][] kernel = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        convolution(kernel, 1.0 / 9);
    }

    public void sharpen() {
        int[][] kernel = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
        convolution(kernel, 1.0);
    }

    public void boxBlur() {
        int[][] kernel = {{1, 1, 1}, {1, 2, 1}, {1, 1, 1}};
        convolution(kernel, 1.0 / 10);
    }

    public void gaussianBlur() {
        int[][] kernel = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
        convolution(kernel, 1.0 / 16);
    }

    public void edgeDetection() {
        int[][] kernel = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
        convolution(kernel, 1.0);
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
        l.saveImage("Lenna");
    }

}
