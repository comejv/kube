package kube.view.panels;

import kube.configuration.ResourceLoader;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class winPanel extends JPanel {
    private float opacity;
    private Image backgroundImage, scaledBackground;
    private Boolean noImage;
    public winPanel() {
        this.noImage = false;
        opacity = 0;
        backgroundImage = ResourceLoader.getBufferedImage("background");

        setOpaque(false);

        // Set the panelFont to the specified custom font
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        if (scaledBackground == null) {
            scaledBackground = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (noImage) {
            g2d.setColor(new Color(128, 128, 128, (int)(opacity * 255 / 2))); // Set the grey color with the specified opacity
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the component with the grey color
        } else {
            g2d.drawImage(scaledBackground, 0, 0, this);
        }
        FontMetrics fm = g2d.getFontMetrics();

        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2d.setColor(new Color(255, 255, 255, 150)); // Set the text color
        Rectangle2D rect = fm.getStringBounds("", g);
        g2d.fillRect(0,
                y - fm.getAscent() -125,
                (int) getWidth(),
                (int) rect.getHeight() + 250);

        g2d.dispose(); // Clean up graphics context
    }
}
