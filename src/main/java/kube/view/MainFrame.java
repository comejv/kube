package kube.view;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import kube.configuration.Config;
import kube.view.panels.GlassPanel;

import java.awt.*;

/*
 * This class initializes the game frame and its layout manager : a card layout that contains all pannels of the game.
 */
public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JComponent glassPane;

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
        createGlassPane(null);
        pack();
        setVisible(true);
    }

    public void addPanel(JPanel panel, String name) {
        cardPanel.add(panel, name);
    }

    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void createGlassPane(Component obj) {
        GlassPanel g = new GlassPanel(obj, cardPanel);
        this.glassPane = g;
        setGlassPane(g);
        g.setVisible(true);
    }

    public void setGlassPaneController(MouseInputAdapter mia) {
        if (glassPane != null) {
            glassPane.addMouseMotionListener(mia);
            glassPane.addMouseListener(mia);
        }
    }

    public void removeGlassPane() {
        if (glassPane != null) {
            remove(getGlassPane());
            glassPane = null;
        }
    }
}
