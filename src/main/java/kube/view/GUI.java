package kube.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import javax.swing.UnsupportedLookAndFeelException;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.DnDController;
import kube.controller.graphical.GUIControllers;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Build;
import kube.model.action.Queue;
import kube.view.panels.*;

public class GUI extends Thread {
    public final static String MENU = "MENU";
    public final static String PHASE1 = "PHASE1";
    public final static String PHASE2 = "PHASE2";

    private MainFrame mF;
    private GUIControllers controllers;
    private Kube k3;

    private volatile FirstPhasePanel firstPhasePanel;
    private volatile SecondPhasePanel secondPhasePanel;
    private Thread loaderThread;
    private Queue<Action> eventsToView;
    private Queue<Action> eventsToModel;

    public GUI(Kube k3, GUIControllers controllers, Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.controllers = controllers;
        this.k3 = k3;
        try {
            boolean nimbusFound = false;
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    Config.debug("Set Look and Feel to Nimbus.");
                    nimbusFound = true;
                    break;
                }
            }
            if (!nimbusFound) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Config.debug("Set Look and Feel to system.");
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |

                IllegalAccessException e) {
            System.err.println("Can't set look and feel : " + e);
        }

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/Jomhuria-Regular.ttf"));
            ge.registerFont(font);
            ge.getAvailableFontFamilyNames();
        } catch (IOException | FontFormatException e) {
            Config.debug("Error : ");
            System.err.println("Could not load buttons font, using default.");
        }

        new Thread(new GUIEventsHandler(this, eventsToView, eventsToModel)).start();

        SwingUtilities.invokeLater(this);
    }

    public void run() {
        // new MainFrame
        mF = new MainFrame();

        // add menu pannel
        MenuPanel mP = new MenuPanel(this, controllers.getMenuController());
        mF.addPanel(mP, MENU);

        if (Config.showBorders()) {
            showAllBorders(mF);
        }

        mF.repaint();
        mF.setFrameVisible(true);

        // After repaint start loading next panel
        loadPanel(GUI.PHASE1);
        createGlassPane();
    }

    public void showPanel(String panelName) {
        waitPanel(panelName);
        mF.showPanel(panelName);
    }

    public void loadPanel(String panelName) {
        PanelLoader loader = new PanelLoader(this, panelName, k3, controllers, eventsToView, eventsToModel);
        loaderThread = new Thread(loader);
        loaderThread.start();
    }

    public synchronized void waitPanel(String panelName) {
        switch (panelName) {
            case GUI.PHASE1:
                try {
                    while (firstPhasePanel == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    System.err.println("Interrupted loading");
                }
                break;

            case GUI.PHASE2:
                try {
                    while (secondPhasePanel == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    System.err.println("Interrupted loading");
                }
                break;
            case GUI.MENU:
                // Do nothing, menu always loaded
                break;
            default:
                System.err.println("Waiting for non existent panel " + panelName);
                break;
        }
        Config.debug("Panel ", panelName, " finished loading");
    }

    public JPanel getOverlay() {
        return mF.getOverlay();
    }

    public void addToOverlay(Component p) {
        mF.addToOverlay(p);
    }

    public void removeAllFromOverlay() {
        mF.removeAllFromOverlay();
    }

    public Component getOverlayComponent() {
        return mF.getOverlayComponent();
    }

    protected void setPanel(String panelName, JPanel panel) {
        switch (panelName) {
            case GUI.PHASE1:
                firstPhasePanel = (FirstPhasePanel) panel;
                break;

            case GUI.PHASE2:
                secondPhasePanel = (SecondPhasePanel) panel;
                break;

            default:
                break;
        }
        mF.addPanel(panel, panelName);
    }

    public void createGlassPane() {
        mF.createGlassPane();
    }

    public void setGlassPaneController(DnDController ma) {
        mF.setGlassPaneController(ma);
    }

    public void setGlassPanelVisible(boolean b) {
        mF.getGlassPane().setVisible(b);
    }

    private void showAllBorders(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                showAllBorders((Container) comp);
            }
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBorder(BorderFactory.createLineBorder(Color.red));
            }
        }
    }

    public void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateFirstPanel(Action a) {
        firstPhasePanel.update(a);
    }

    public void updateSecondPanel(Action a) {
        secondPhasePanel.update(a);
    }
}
