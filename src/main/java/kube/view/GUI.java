package kube.view;

import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import kube.configuration.Config;
import kube.view.panels.*;

public class GUI extends Thread {
    public final static String MENU = "MENU";
    public final static String PHASE1 = "PHASE1";
    public final static String PHASE2 = "PHASE2";

    MainFrame mF;
    ActionListener mL, fL, sL;

    public GUI(ActionListener mL, ActionListener fL, ActionListener sL) {
        this.mL = mL;
        this.fL = fL;
        this.sL = sL;
    }

    public static GUI start(ActionListener mL, ActionListener fL, ActionListener sL) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            Config.debug("Set Look and Feel to system.");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            System.err.println("Can't set look and feel : " + e);
        }
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
        GUI gui = new GUI(mL, fL, sL);
        SwingUtilities.invokeLater(gui);
        return gui;
    }

    public void run() {
        // new MainFrame
        mF = new MainFrame();

        // add menu pannel
        MenuPanel mP = new MenuPanel(mL);
        mF.addPanel(mP, MENU);
        // add new phase 1 pannel
        FirstPhasePanel fP = new FirstPhasePanel(fL);
        mF.addPanel(fP, PHASE1);
        // add new phase 2 pannel
        SecondPhasePanel sP = new SecondPhasePanel(sL);
        mF.addPanel(sP, PHASE2);
    }

    public void showPanel(String name) {
        mF.showPanel(name);
    }
}
