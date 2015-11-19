package me.imsean.ptpbot.api.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Guyu Fan
 */

public class CharsInfo {

    private class Char {
        private Font font;
        private String ch;
        double grayscale;

        public Char(String ch, Font f) {
            this.ch = ch;
            this.font = f;

            BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = temp.createGraphics();
            FontMetrics fm = g2d.getFontMetrics(font);
            g2d.dispose();

            int width = fm.stringWidth(ch);
            int height = fm.getHeight();
            int y = fm.getAscent();

            BufferedImage charImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = charImg.createGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(ch, 0, y);
            g.dispose();

            int totalRGB = 0;
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++) {
                    Color pix = new Color(charImg.getRGB(i, j));
                    totalRGB += pix.getRed() + pix.getGreen() + pix.getBlue();
                }

            grayscale = ((double) totalRGB) / (3 * width * height);
        }

        @Override
        public String toString() {
            return ch + " "  + grayscale;
        }
    }

    private Char[] chars;
    final double Y_VS_X_RATIO;

    public CharsInfo(String s, Font f) {
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = temp.createGraphics();
        FontMetrics fm = g2d.getFontMetrics(f);
        int width = fm.charWidth(' ');
        int height = fm.getHeight();
        Y_VS_X_RATIO = ((double) height) / width;

        chars = new Char[s.length()];
        for (int i = 0; i < s.length(); i++)
            chars[i] = new Char(s.substring(i, i + 1), f);
        sort();
        weight();
    }

    private void sort() {
        double minGrayscale;
        int minIndex;
        int j;

        for (int i = 0; i < chars.length; i++) {
            minGrayscale = 300;
            minIndex = i;
            for (j = i; j < chars.length; j++)
                if (chars[j].grayscale < minGrayscale) {
                    minGrayscale = chars[j].grayscale;
                    minIndex = j;
                }
            Char temp = chars[i];
            chars[i] = chars[minIndex];
            chars[minIndex] = temp;
        }
    }

    private void weight() {
        double factor = 255 / (chars[chars.length - 1].grayscale
                - chars[0].grayscale);

        double grayscale = chars[0].grayscale;
        chars[0].grayscale = 0;
        for (int i = 1; i < chars.length; i++) {
            double diff = chars[i].grayscale - grayscale;
            grayscale = chars[i].grayscale;
            chars[i].grayscale = chars[i - 1].grayscale + factor * diff;
        }
    }

    private double differnce(double a, double b) {
        double diff = a - b;
        if (diff >= 0) return diff;
        return 0 - diff;
    }

    public String match(double grayscale) {
        String ch = "match failed";
        double leastDiff = 300;
        for (Char c: chars) {
            double diff = differnce(grayscale, c.grayscale);
            if (diff < leastDiff) {
                leastDiff = diff;
                ch = c.ch;
            }
        }

        return ch;
    }

}