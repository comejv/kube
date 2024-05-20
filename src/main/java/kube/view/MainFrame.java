package kube.view;

import javax.swing.*;

import kube.configuration.Config;

import java.awt.*;

/*
 * This class initializes the game frame and its layout manager : a card layout that contains all pannels of the game.
 */
public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel framePanel;
    private Component overlay;

    public MainFrame() {
        setTitle("KUBE");
        // setIconImage(); for later
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.getInitWidth(), Config.getInitHeight()));
        setMinimumSize(new Dimension((int) (Config.getInitWidth() / 1.5), Config.getInitHeight()));
        setLocationRelativeTo(null);
        framePanel = new JPanel();
        OverlayLayout overlay = new OverlayLayout(framePanel);
        framePanel.setLayout(overlay);
        framePanel.setVisible(true);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        framePanel.add(cardPanel);
        add(framePanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void addPanel(JPanel panel, String name) {
        cardPanel.add(panel, name);
    }

    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void addOverlay(Component p) {
        Config.debug("Added new overlay");
        framePanel.add(p);
        p.setVisible(true);
        overlay = p;
        framePanel.revalidate();
        framePanel.repaint();
    }

    public void removeOverlay() {
        if (overlay != null) {
            framePanel.remove(overlay);
            framePanel.revalidate();
            framePanel.repaint();
            overlay = null;
        }
    }
}
