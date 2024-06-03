package kube.view.panels;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;


import javax.swing.*;
import java.awt.*;

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
        // g2d.drawImage(backgroundImage, 0, 0, this);


        g2d.dispose(); // Clean up graphics context
    }
}
