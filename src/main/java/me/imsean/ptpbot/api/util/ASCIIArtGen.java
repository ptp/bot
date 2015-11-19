package me.imsean.ptpbot.api.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Guyu Fan
 */

public class ASCIIArtGen {

    private static final String ALL_CHAR =
            "!#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLM" +
                    "NOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~ ";
    private CharsInfo ci;
    private int size;

    private BufferedImage original;
    private BufferedImage scaled;
    private String ASCIIart;

    public ASCIIArtGen(Font artFont) {
        ci = new CharsInfo(ALL_CHAR, artFont);
    }

    private void scaleImage() {
        int width = original.getWidth();
        int height = (int) (original.getHeight() / ci.Y_VS_X_RATIO);

        int newWidth, newHeight;

        if (width <= size && height <= size) {
            newWidth = width;
            newHeight = height;
        } else {
            if (width > height) {
                newWidth = size;
                newHeight = (int) (height * ((double) size / width));
            } else {
                newWidth = (int) (width * ((double) size / height));
                newHeight = size;
            }
        }

        Image temp = original.getScaledInstance(
                newWidth, newHeight, Image.SCALE_SMOOTH);
        scaled = new BufferedImage(
                newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaled.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();
    }

    private void generateArt() {
        ASCIIart = "";

        for (int y = 0; y < scaled.getHeight(); y++) {
            for (int x = 0; x < scaled.getWidth(); x++) {
                Color pixel = new Color(scaled.getRGB(x, y));
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                double grayscale = (r + g + b) / 3.0;
//                double grayscale = r * 0.2989 + g * 0.5870 + b * 0.1140;
                String charStr = ci.match(grayscale);
                ASCIIart += charStr;
            }
            ASCIIart += "\n";
        }
    }

    public String convert(BufferedImage img, int artSize) {
        if (img == null)
            throw new NullPointerException();

        if (artSize < 1)
            throw new IllegalArgumentException();

        size = artSize;
        original = img;
        scaleImage();
        generateArt();

        return ASCIIart;
    }

    public BufferedImage export(String art, Font font) {
        String[] lines = art.split("\n");

        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D tempGraphics = temp.createGraphics();
        FontMetrics fm = tempGraphics.getFontMetrics(font);
        tempGraphics.dispose();

        int width = fm.stringWidth(lines[0]);
        int height = lines.length * fm.getHeight();

        BufferedImage artImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = artImg.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setFont(font);
        g.setColor(Color.BLACK);
        int y = fm.getAscent();
        for (String line: lines) {
            g.drawString(line, 0, y);
            y += fm.getHeight();
        }
        g.dispose();

        return artImg;
    }

}