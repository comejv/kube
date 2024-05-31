package kube.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.model.action.ActionType;
import kube.view.GUI;

public class OverlayPanel extends JPanel{

    public OverlayPanel(GUI gui, MenuController buttonListener, ActionType action){

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(Config.INIT_WIDTH, Config.INIT_HEIGHT));
        setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints center = new GridBagConstraints();
        center.anchor = GridBagConstraints.CENTER;
        
        switch (action) {
            case SETTINGS:
                add(new SettingsPanel(gui, buttonListener), center);
                break;
            case RULES:
                add(new RulesPanel(gui, buttonListener), center);
                break;
            default:
                break;
        }
    }
}
