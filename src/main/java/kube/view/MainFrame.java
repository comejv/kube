package kube.view;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import kube.configuration.Config;
import kube.controller.graphical.DnDController;
import kube.view.panels.GlassPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;

/*
 * This class initializes the game frame and its layout manager : an overlay layout that contains a card layout and potential overlay elements.
 */
public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JComponent glassPane;
    private JPanel framePanel;
    private JPanel overlayPanel;
    private Component overlay;

    public MainFrame() {
        setTitle("KUBE");
        // setIconImage(); for later
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.getInitWidth(), Config.getInitHeight()));
        setMinimumSize(new Dimension((int) (Config.getInitWidth() / 1.5), Config.getInitHeight()));
        setLocationRelativeTo(null);
        framePanel = (JPanel) getContentPane();
        OverlayLayout overlay = new OverlayLayout(framePanel);
        framePanel.setLayout(overlay);
        framePanel.setVisible(true);
        overlayPanel = new JPanel();
        overlayPanel.setVisible(false);
        overlayPanel.setOpaque(false);
        overlayPanel.setSize(new Dimension(Config.getInitWidth(), Config.getInitHeight()));
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        framePanel.add(overlayPanel);
        framePanel.add(cardPanel);
        pack();
        setVisible(true);
    }

    public void addPanel(JPanel panel, String name) {
        cardPanel.add(panel, name);
    }

    public void showPanel(String name) {
        Config.debug("Showing panel ", name);
        cardLayout.show(cardPanel, name);
    }

    public void createGlassPane() {
        if (glassPane != null) {
            System.err.println("Glass pane already exists.");
            return;
        }
        GlassPanel g = new GlassPanel();
        this.glassPane = g;
        super.setGlassPane(g);
        g.setVisible(true);
    }

    public void setGlassPaneController(DnDController ma) {
        glassPane.addMouseMotionListener(ma);
        glassPane.addMouseListener(ma);
    }

    public void removeGlassPane() {
        if (glassPane == null) {
            System.err.println("No glass pane exists.");
            return;
        }
        remove(getGlassPane());
        glassPane = null;
    }

    public void addOverlay(Component p) {
        if (p != null) {
            System.err.println("Overlay already exists.");
            return;
        }
        overlayPanel.add(p);
        overlayPanel.setVisible(true);
        framePanel.revalidate();
        framePanel.repaint();
        overlay = p;
    }

    public void removeOverlay() {
        if (overlay == null) {
            System.err.println("No existing overlay.");
            return;
        }
        overlayPanel.remove(overlay);
        overlayPanel.setVisible(false);
        framePanel.revalidate();
        framePanel.repaint();
        overlay = null;
    }

    public Component getOverlayComponent() {
        return overlay;
    }
}
