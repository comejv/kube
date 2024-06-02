package kube.view.panels;

import java.awt.*;
import javax.swing.*;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;

public class TransparentPanel extends JPanel {
    private String overlayText;
    private float opacity;
    private String text;
    private Image backgroundImage, scaledBackground;
    private Font panelFont; // Font variable to store the custom font
    private Boolean noImage;

    public TransparentPanel(String text, Boolean noImage) {
        this(text);
        this.noImage = true;
    }

    public TransparentPanel(String text) {
        this.noImage = false;
        this.text = text;
        opacity = 0;
        backgroundImage = ResourceLoader.getBufferedImage("background");

        setOpaque(false);

        // Set the panelFont to the specified custom font
        panelFont = new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT * Config.getUIScale() / 10));
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    public void setText(String text) {
        this.text = text;
        repaint(); // Ensure the panel repaints when the text is changed
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (scaledBackground == null) {
            scaledBackground = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        }
        super.paintComponent(g);
        Config.debug("Start redraw", opacity);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (noImage) {
            Config.debug("no image");
            g2d.setColor(new Color(128, 128, 128, (int)(opacity * 255 / 2))); // Set the grey color with the specified opacity
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the component with the grey color
        } else {
            g2d.drawImage(scaledBackground, 0, 0, this);
        }
        // g2d.drawImage(backgroundImage, 0, 0, this);

        // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(Color.BLACK); // Set the text color
        g2d.setFont(panelFont); // Set the custom font

        // Draw the text in the center of the panel
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);

        g2d.dispose(); // Clean up graphics context
        Config.debug("Stop redraw");
    }
}
