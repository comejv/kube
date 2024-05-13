package kube.view;

import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import javax.swing.UnsupportedLookAndFeelException;

import kube.configuration.Config;
import kube.view.panels.*;

public class GUI extends Thread {
    public final static String MENU = "MENU";
    public final static String PHASE1 = "PHASE1";
    public final static String PHASE2 = "PHASE2";

    MainFrame mF;
    ActionListener menuListener, firstPhaseListener, secondPhaseListener;

    public GUI(ActionListener mL, ActionListener fL, ActionListener sL) {
        this.menuListener = mL;
        this.firstPhaseListener = fL;
        this.secondPhaseListener = sL;
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
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            System.err.println("Can't set look and feel : " + e);
        }
        SwingUtilities.invokeLater(this);
    }

    public void run() {
        // new MainFrame
        mF = new MainFrame();

        // add menu pannel
        MenuPanel mP = new MenuPanel(menuListener);
        mF.addPanel(mP, MENU);
        // add new phase 1 pannel
        FirstPhasePanel fP = new FirstPhasePanel(firstPhaseListener);
        mF.addPanel(fP, PHASE1);
        // add new phase 2 pannel
        SecondPhasePanel sP = new SecondPhasePanel(secondPhaseListener);
        mF.addPanel(sP, PHASE2);
    }

    public void showPanel(String name) {
        mF.showPanel(name);
    }
}
