package kube.view.components;

import kube.configuration.Config;
import kube.view.GUIColors;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

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

    // private static class RoundedBorder implements Border {

    // private int radius;

    // RoundedBorder(int radius) {
    // this.radius = radius;
    // }

    // public Insets getBorderInsets(Component c) {
    // return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    // }

    // public boolean isBorderOpaque() {
    // return true;
    // }

    // public void paintBorder(Component c, Graphics g, int x, int y, int width, int
    // height) {
    // g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    // }
    // }

}
