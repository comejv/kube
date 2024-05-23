package kube.view;

import javax.swing.*;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.panels.GlassPanel;

import java.awt.*;

/*
 * This class initializes the game frame and its layout manager : a card layout that contains all pannels of the game.
 */
public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("KUBE");
        // setIconImage(); for later
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.getInitWidth(), Config.getInitHeight()));
        setMinimumSize(new Dimension((int) (Config.getInitWidth() / 1.5), Config.getInitHeight()));
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        cardPanel = (JPanel) getContentPane();
        cardPanel.setLayout(cardLayout);
        pack();
        setVisible(true);
    }

    public void addPanel(JPanel panel, String name) {
        cardPanel.add(panel, name);
    }

    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void createGlassPane(Component obj){
        setGlassPane(new GlassPanel(obj, cardPanel));
        getGlassPane().setVisible(true);
    }

    public void removeGlassPane(){
        remove(getGlassPane());
    }
}
