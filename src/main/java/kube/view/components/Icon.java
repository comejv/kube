package kube.view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import kube.view.HSL;

public class Icon extends JPanel {
    private BufferedImage originalImage;

    public Icon(BufferedImage bufferedImage) {
        super();
        this.originalImage = bufferedImage;
        setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
        setOpaque(false);
    }

    public Icon(BufferedImage bufferedImage, boolean actionable) {
        this(bufferedImage);
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

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int c = originalImage.getRGB(x, y);
                if (c == 0) {
                    continue;
                }
                HSL imgHsl = new HSL(c);
                imgHsl.setHue(hsl.getHue());
                originalImage.setRGB(x, y, c & (0xFF000000) | imgHsl.toRGB());
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
