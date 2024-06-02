package kube.view.panels;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import kube.configuration.ResourceLoader;

import java.io.File;
import java.io.IOException;

public class OpacityExample extends JFrame {

    public OpacityExample() {
        setTitle("Opacity Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create a custom panel for the background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% opacity
                g2d.drawImage(ResourceLoader.getBufferedImage("background"), 0, 0, null); // Draw the background image
                super.paintComponent(g2d);
            }
        };
        backgroundPanel.setOpaque(false); // Make the background panel non-opaque

        // Create a custom panel for the text
        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Full opacity
                super.paintComponent(g2d);
                g2d.setColor(Color.BLACK);
                g2d.drawString("This is an announcement!", 20, 150);
            }
        };

        // Layout the panels
        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OpacityExample::new);
    }
}