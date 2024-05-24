package kube.view;

import java.io.IOException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import javax.swing.UnsupportedLookAndFeelException;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.GUIControllers;
import kube.model.Game;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.panels.*;

public class GUI extends Thread {
    public final static String MENU = "MENU";
    public final static String PHASE1 = "PHASE1";
    public final static String PHASE2 = "PHASE2";

    private MainFrame mF;
    private GUIControllers controllers;
    private Game model;

    private volatile FirstPhasePanel firstPhasePanel;
    private volatile SecondPhasePanel secondPhasePanel;
    private Thread loaderThread;

    public GUI(Game model, GUIControllers controllers, Queue<Action> events) {
        this.controllers = controllers;
        this.model = model;
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

        new Thread(new GUIEventsHandler(this, events)).start();

        SwingUtilities.invokeLater(this);
    }

    public void run() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font buttonsFont = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/Jomhuria-Regular.ttf"));

            ge.registerFont(buttonsFont);
            ge.getAvailableFontFamilyNames();
        } catch (IOException | FontFormatException e) {
            Config.debug("Error : ");
            System.err.println("Could not load buttons font, using default.");
        }
        // new MainFrame
        mF = new MainFrame();

        // add menu pannel
        MenuPanel mP = new MenuPanel(controllers.getMenuController());
        mF.addPanel(mP, MENU);

        if (Config.showBorders()) {
            showAllBorders(mF);
        }

        mF.pack();
        mF.repaint();

        // After repaint start loading next panel
        loadPanel(GUI.PHASE1);
    }

    public void showPanel(String panelName) {
        waitPanel(panelName);
        mF.showPanel(panelName);
    }

    public void loadPanel(String panelName) {
        PanelLoader loader = new PanelLoader(this, panelName, model, controllers);
        loaderThread = new Thread(loader);
        loaderThread.start();
    }

    public synchronized void waitPanel(String panelName) {
        Config.debug("Waiting for panel ", panelName);
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

            default:
                System.err.println("Waiting for non existent panel " + panelName);
                break;
        }
        Config.debug("Panel ", panelName, " finished loading");
    }

    public void addOverlay(Component p) {
        mF.addOverlay(p);
    }

    public void removeOverlay() {
        mF.removeOverlay();
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
}
