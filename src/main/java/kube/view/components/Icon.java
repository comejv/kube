package kube.view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import kube.configuration.Config;
import kube.view.HSL;

public class Icon extends JLabel {

    // TODO : refactor this class to make it more readable
    private BufferedImage originalImage;
    private BufferedImage img;
    private HSL color;

    public Icon(BufferedImage bufferedImage) {
        super();
        this.originalImage = bufferedImage;
        this.img = bufferedImage;
        setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(getImage(), 0, 0, null);
        g2d.dispose();
    }

    public void recolor(HSL hsl) {
        this.color = hsl;
        int width = getImage().getWidth();
        int height = getImage().getHeight();
        BufferedImage recoloredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = getImage().getRGB(x, y);
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
        img = recoloredImage;
        repaint();
    }

    public void resizeIcon(int width, int height) {
        Image resized = getOriginalImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        Graphics2D g2d = getImage().createGraphics();
        g2d.drawImage(resized, 0, 0, null);
        g2d.dispose();
        setPreferredSize(new Dimension(width, height));
        if (color != null) {
            recolor(color);
        }
        repaint();
    }

    public BufferedImage getImage() {
        return img;
    }

    public void setImage(BufferedImage img) {
        this.img = img;
    }

    protected BufferedImage getOriginalImage() {
        return originalImage;
    }

    public int getImageWidth() {
        return getImage().getWidth();
    }

    public int getImageHeight() {
        return getImage().getHeight();
    }
}
