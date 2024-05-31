package kube.view;

import javax.swing.*;

import kube.configuration.Config;
import kube.controller.graphical.Phase1DnD;
import kube.view.panels.GlassPanel;

import java.awt.*;
import java.awt.event.ActionListener;

/*
 * This class initializes the game frame and its layout manager : an overlay layout that contains a card layout and potential overlay elements.
 */
public class MainFrame extends JFrame {
    // TODO : refactor this class to make it more readable
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JComponent glassPane;
    private JPanel framePanel;
    private JPanel overlayPanel;
    private Component overlay;
    private Phase1DnD currentListener;

    public MainFrame() {
        setTitle("KUBE");
        // setIconImage(); for later
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.INIT_WIDTH, Config.INIT_HEIGHT));
        setMinimumSize(new Dimension((int) (Config.INIT_WIDTH / 1.5), Config.INIT_HEIGHT));
        setLocationRelativeTo(null);
        framePanel = (JPanel) getContentPane();
        OverlayLayout overlay = new OverlayLayout(framePanel);
        framePanel.setLayout(overlay);
        framePanel.setVisible(true);
        overlayPanel = new JPanel();
        overlayPanel.setVisible(false);
        overlayPanel.setOpaque(false);
        overlayPanel.setSize(new Dimension(Config.INIT_WIDTH, Config.INIT_HEIGHT));
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        framePanel.add(overlayPanel);
        framePanel.add(cardPanel);
    }

    public void resize(){
        setSize(Config.INIT_WIDTH, Config.INIT_HEIGHT);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public void addPanel(JPanel panel, String name) {
        cardPanel.add(panel, name);
    }

    public void setFrameVisible(boolean b) {
        setVisible(true);
    }

    public void showPanel(String name) {
        Config.debug("Showing panel ", name);
        cardLayout.show(cardPanel, name);
    }

    public void createGlassPane() {
        if (glassPane != null) {
            Config.error("Glass pane already exists.");
            return;
        }
        GlassPanel g = new GlassPanel();
        this.glassPane = g;
        super.setGlassPane(g);
    }

    public void setGlassPaneController(Phase1DnD ma) {
        if (currentListener != null){
            glassPane.removeMouseMotionListener(currentListener);
            glassPane.removeMouseListener(currentListener);
            glassPane.removeMouseWheelListener(currentListener);
        }
        glassPane.addMouseMotionListener(ma);
        glassPane.addMouseListener(ma);
        glassPane.addMouseWheelListener(ma);
        currentListener = ma;
    }

    public void removeGlassPane() {
        if (glassPane == null) {
            Config.error("No glass pane exists.");
            return;
        }
        remove(getGlassPane());
        glassPane = null;
    }
    
    public JPanel getOverlay() {
        return overlayPanel;
    }

    public JPanel getFramePanel(){
        return framePanel;
    }
    
    public void addToOverlay(Component p) {
        overlayPanel.add(p);
        overlayPanel.setVisible(true);
        framePanel.revalidate();
        framePanel.repaint();
        overlay = p;
    }

    public void removeAllFromOverlay() {
        if (overlay != null) {
            overlayPanel.removeAll();
            overlayPanel.setVisible(false);
            framePanel.revalidate();
            framePanel.repaint();
            overlay = null;
        }
    }

    public Component getOverlayComponent() {
        return overlay;
    }
}
