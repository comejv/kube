package kube.view.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import kube.controller.graphical.MenuController;
import kube.model.action.ActionType;
import kube.view.GUI;

public class OverlayPanel extends JPanel{

    private GUI gui;

    public OverlayPanel(GUI gui, MenuController buttonListener, ActionType action){

        this.gui = gui;
        setLayout(new GridBagLayout());
        setPreferredSize(gui.getMainFrame().getSize());
        setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints center = new GridBagConstraints();
        center.anchor = GridBagConstraints.CENTER;
        
        switch (action) {
            case SETTINGS:
                add(new SettingsPanel(gui, buttonListener), center);
                break;
            case LOAD_PANEL:
                add(new LoadingSavePanel(gui, buttonListener), center);
                break;
            case RULES:
                add(new RulesPanel(gui, buttonListener), center);
                break;
            default:
                break;
        }

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            }
        };

        addMouseListener(ma);
    }
}