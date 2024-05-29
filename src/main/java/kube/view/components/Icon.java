package kube.view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import kube.view.HSL;

public class Icon extends JLabel {
    private BufferedImage originalImage;

    public Icon(BufferedImage bufferedImage) {
        super();
        this.originalImage = bufferedImage;
        setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();
    }

    public void recolor(HSL hsl) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage recoloredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                if (alpha == 0) {
                    continue; // Skip transparent pixels
                }

                HSL pixelHsl = new HSL(rgb);
                pixelHsl.setHue(hsl.getHue());
                pixelHsl.setSaturation(hsl.getSaturation());
                pixelHsl.setLuminance(hsl.getLuminance() * pixelHsl.getLuminance());
                recoloredImage.setRGB(x, y, (alpha << 24) | pixelHsl.toRGB());
            }
        }
        originalImage = recoloredImage;
        repaint();
    }

    public void resizeIcon(int width, int height) {
        Image resized = originalImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        originalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = originalImage.createGraphics();
        g2d.drawImage(resized, 0, 0, null);
        g2d.dispose();
        setPreferredSize(new Dimension(width, height));
        repaint();
    }

    protected BufferedImage getImage() {
        return originalImage;
    }
}
