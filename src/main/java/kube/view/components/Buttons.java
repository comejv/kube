package kube.view.components;

import kube.configuration.Config;
import kube.view.GUIColors;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 10)));
        }
    }

    public static class LocalButton extends JButton {
        public LocalButton(String name) {
            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth() / 2, Config.getInitWidth() / 6));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class GameFirstPhaseButton extends JButton {
        public GameFirstPhaseButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class GameSecondPhaseButton extends JButton {
        public GameSecondPhaseButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class ParameterButton extends JButton {
        public ParameterButton(Image image) {
            super();
            Image scaledImg = image.getScaledInstance(
                    (int) (0.15 * Config.getInitHeight()),
                    (int) (0.15 * Config.getInitHeight()),
                    Image.SCALE_SMOOTH);

            ImageIcon icon = new ImageIcon(scaledImg);
            setIcon(icon);

            setPreferredSize(new Dimension((int) (0.20 * Config.getInitHeight()),
                    (int) (0.20 * Config.getInitHeight())));

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }
    }

    public static class RulesButton extends JButton {
        public RulesButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class InGameMenuButton extends JButton {
        public InGameMenuButton(String name) {
            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth() / 4, Config.getInitHeight() / 8));
            setBackground(GUIColors.ACCENT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 15)));
        }
    }

    public static class ButtonIcon extends Icon {
        private final String name;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public ButtonIcon(String name, BufferedImage image, ActionListener mouseListener) {
            super(image);
            this.name = name;

            // Apply the provided MouseListener
            addMouseListener((MouseListener) mouseListener);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Darken the image on hover
            float factor = isHovered ? (isPressed ? 0.75f : 1.25f) : 1.0f;

            if (isHovered) { // Draw darker image
                float[] scales = { factor };
                float[] offsets = new float[4];
                RescaleOp rop = new RescaleOp(scales, offsets, null);
                g2d.drawImage(getImage(), rop, 0, 0);
            } else { // Draw the original image
                g2d.drawImage(getImage(), 0, 0, null);
            }

        }

        public void setHovered(boolean b) {
            isHovered = b;
            repaint();
        }

        public void setPressed(boolean b) {
            isPressed = b;
            repaint();
        }

        public void setDefault() {
            isHovered = false;
            isPressed = false;
            repaint();
        }

        public String getName() {
            return name;
        }

    }
}
