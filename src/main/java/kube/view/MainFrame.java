package kube.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.components.Icon;
import kube.view.panels.GlassPanel;

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
    private MouseAdapter currentListener;
    private MouseAdapter defaultGlassPaneController;

    public MainFrame() {
        setTitle("KUBE");
        setIconImage(ResourceLoader.getBufferedImage("hexaBlueTextured"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.INIT_WIDTH, Config.INIT_HEIGHT));
        setMinimumSize(new Dimension((int) (Config.INIT_WIDTH / 1.5), (int) (Config.INIT_HEIGHT / 1.5)));
        setLocationRelativeTo(null);
        defaultGlassPaneController = null;
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

    public void resize() {
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
        g.addMouseListener(defaultGlassPaneController);
        g.addMouseMotionListener(defaultGlassPaneController);
        g.addMouseWheelListener(defaultGlassPaneController);
        this.glassPane = g;
        super.setGlassPane(g);
    }

    public void setGlassPaneController(MouseAdapter ma) {
        if (currentListener != null) {
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

    public JPanel getFramePanel() {
        return framePanel;
    }

    public MouseAdapter getCurrentListener() {
        return currentListener;
    }

    public MouseAdapter getDefaultGlassPaneController() {
        return defaultGlassPaneController;
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

    public void incrementUIScale(double factor) {
        Config.error("UI scaling has been disabled");
        // Config.setUIScale(Config.getUIScale() * factor);
        // resizeComponents(this.getContentPane(), factor);
    }

    public void resetUIScale() {
        double currentScale = Config.getUIScale();
        double inverseScale = 1.0 / currentScale;
        Config.resetUIScale();
        resizeComponents(this.getContentPane(), inverseScale);
    }

    public void resizeComponents(Container container, double factor) {
        for (Component component : container.getComponents()) {
            // Resize the component
            Dimension size = component.getSize();
            Dimension preferredSize = component.getPreferredSize();
            Dimension minimumSize = component.getMinimumSize();
            Dimension maximumSize = component.getMaximumSize();

            component.setSize(new Dimension((int) (size.width * factor), (int) (size.height * factor)));
            component.setPreferredSize(
                    new Dimension((int) (preferredSize.width * factor), (int) (preferredSize.height * factor)));
            component.setMinimumSize(
                    new Dimension((int) (minimumSize.width * factor), (int) (minimumSize.height * factor)));
            component.setMaximumSize(
                    new Dimension((int) (maximumSize.width * factor), (int) (maximumSize.height * factor)));

            if (component instanceof JComponent) {
                Font font = component.getFont();
                if (font != null) {
                    component.setFont(font.deriveFont((float) (font.getSize2D() * factor)));
                }
                if (component instanceof Icon) {
                    Icon i = (Icon) component;
                    i.resizeIcon((int) (i.getImageWidth() * factor), (int) (i.getImageHeight() * factor));
                }
            }

            // If the component is a container, resize its components recursively
            if (component instanceof Container) {
                resizeComponents((Container) component, factor);
            }
        }
        revalidate();
        repaint();
    }
}
