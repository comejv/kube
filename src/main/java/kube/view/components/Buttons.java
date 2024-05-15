package kube.view.components;

import kube.configuration.Config;
import kube.view.GUIColors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

/*
 * This class will have subclasses for all the buttons used.
 */
public class Buttons {

    public static class MenuButton extends JButton {
        public MenuButton(String name) {
            super(name);

            setPreferredSize(new Dimension(Config.getInitWidth() / 2,
                    Config.getInitWidth() / 12));
            setBackground(GUIColors.ACCENT);
            setForeground(GUIColors.TEXT);

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 10)));
        }
    }

    public static class LocalButton extends JButton {
        public LocalButton(String name) {
            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth() / 2, Config.getInitWidth() / 6));
            setBackground(GUIColors.ACCENT);
            setForeground(GUIColors.TEXT);

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class GameFirstPhaseButton extends JButton {
        public GameFirstPhaseButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT);
            setForeground(GUIColors.TEXT);

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class GameSecondPhaseButton extends JButton {
        public GameSecondPhaseButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT);

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class ParameterButton extends JButton {
        public ParameterButton(Image image) {
            super(new ImageIcon(image));
            setPreferredSize(new Dimension(100, 100));
        }
    }

    public static class RulesButton extends JButton {
        public RulesButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT);

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class InGameMenuButton extends JButton {
        public InGameMenuButton(String name) {
            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth() / 4, Config.getInitHeight() / 8));
            setBackground(GUIColors.ACCENT);

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class ButtonIcon extends JLabel {
        private BufferedImage originalImage;
        private final String name;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public ButtonIcon(String name, BufferedImage bufferedImage, ActionListener mouseListener) {
            super();
            this.originalImage = bufferedImage;
            this.name = name;
            setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
            setOpaque(false);

            // Apply the provided MouseListener
            addMouseListener((MouseListener) mouseListener);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Darken the image on hover
            float factor = isHovered ? (isPressed ? 0.75f : 1.1f) : 1.0f;

            if (isHovered) { // Draw darker image
                float[] scales = { factor };
                float[] offsets = new float[4];
                RescaleOp rop = new RescaleOp(scales, offsets, null);
                g2d.drawImage(originalImage, rop, 0, 0);
            } else { // Draw the original image
                g2d.drawImage(originalImage, 0, 0, null);
            }

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
                    double[] hsl = GUIColors.rgbToHsl(originalImage.getRGB(x, y));
                    hsl[0] = GUIColors.rgbToHsl(c.getRGB())[0];
                    // hsl[0] = 0.3;
                    int[] rgbArray = GUIColors.hslToRgb(hsl);
                    int rgb = originalImage.getRGB(x, y) & (0xFF << 24) // Keep alpha
                            | (rgbArray[0] << 16) | (rgbArray[1] << 8) | rgbArray[2]; // Set RGB
                    originalImage.setRGB(x, y, rgb);
                }
            }
            repaint();
        }

        public void setHovered(boolean b) {
            isHovered = b;
            repaint();
        }

        public void setPressed(boolean b) {
            isPressed = b;
            repaint();
        }

        public String getName() {
            return name;
        }
    }
}
