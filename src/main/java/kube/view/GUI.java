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

    MainFrame mF;
    GUIControllers controllers;
    Game model;

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
        MenuPanel mP = new MenuPanel(this, controllers.getMenuController());
        mF.addPanel(mP, MENU);
        // add new phase 1 pannel
        FirstPhasePanel fP = new FirstPhasePanel(model, controllers.getPhase1Controller());
        mF.addPanel(fP, PHASE1);
        // add new phase 2 pannel
        SecondPhasePanel sP = new SecondPhasePanel(model, controllers.getPhase2Controller());
        mF.addPanel(sP, PHASE2);

        if (Config.showBorders()) {
            showAllBorders(mF);
        }

        mF.pack();
        mF.repaint();
    }

    public void revalidate(){
        mF.revalidate();
    }

    public void repaint() {
        mF.repaint();
    }

    public void showPanel(String name) {
        mF.showPanel(name);
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

    public MainFrame getMainFrame(){
        return mF;
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
