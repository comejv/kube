package kube.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import kube.view.GUIColors;

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

    public void recolor(Color c) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (originalImage.getRGB(x, y) == 0) {
                    continue;
                }
                double[] imgHsl = GUIColors.rgbToHsl(originalImage.getRGB(x, y));
                double[] colHsl = GUIColors.rgbToHsl(c.getRGB());
                if (imgHsl[2] == 0) {
                    imgHsl[2] = colHsl[2];
                }
                imgHsl[0] = colHsl[0];
                int[] rgbArray = GUIColors.hslToRgb(imgHsl);
                int rgb = originalImage.getRGB(x, y) & (0xFF << 24) // Keep alpha
                        | (rgbArray[0] << 16) | (rgbArray[1] << 8) | rgbArray[2]; // Set RGB
                originalImage.setRGB(x, y, rgb);
            }
        }
        repaint();
    }

    public void resizeIcon(int width, int height) {
        Image resized = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
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
