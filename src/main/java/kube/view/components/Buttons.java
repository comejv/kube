package kube.view.components;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
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

    public static class MenuButtons extends JButton {
        public MenuButtons(String name){
            //Font font = Font.createFont(Font.TRUETYPE_FONT, getResourceAsStream(Jomhuria-Regular.ttf));

            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth()/3, Config.getInitWidth()/6));
            setBackground(GUIColors.ACCENT);
            setForeground(GUIColors.TEXT);

            //setFont(new Font(font, Font.PLAIN, 40));
        }
    }

    public static class LocalButtons extends JButton {
        public LocalButtons(String name){
            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth()/2, Config.getInitWidth()/6));
            setBackground(GUIColors.ACCENT);
            setForeground(GUIColors.TEXT);
        }    
    }

    public static class GameFirstPhaseButtons extends JButton {
        public GameFirstPhaseButtons(String name){
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT);
            setForeground(GUIColors.TEXT);
        }    
    }

    public static class GameSecondPhaseButtons extends JButton {
        public GameSecondPhaseButtons(String name){
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT);
        }
    }

    public static class ParameterButtons extends JButton {
        public ParameterButtons(Image image){
            super(new ImageIcon(image));
            setPreferredSize(new Dimension(100, 100));
        }
    }

    public static class RulesButtons extends JButton {
        public RulesButtons(String name){
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT);
        }
    }

    public static class InGameMenuButtons extends JButton {
        public InGameMenuButtons(String name){
            super(name);
            setPreferredSize(new Dimension(Config.getInitWidth()/4, Config.getInitHeight()/8));
            setBackground(GUIColors.ACCENT);
        }
    }


    // private static class RoundedBorder implements Border {

    //     private int radius;

    //     RoundedBorder(int radius) {
    //         this.radius = radius;
    //     }

    //     public Insets getBorderInsets(Component c) {
    //         return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    //     }

    //     public boolean isBorderOpaque() {
    //         return true;
    //     }

    //     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    //         g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    //     }
    // }   

}
