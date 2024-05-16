package kube.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import kube.view.GUI;
import kube.configuration.Config;
import kube.view.components.Buttons.ButtonIcon;

/*
 * This class will create the view and handle user inputs through listeners implemented as subclasses.
 */
public class MainController {
    private GUI gui;
    private Game game;

    public MainController() {
        game = new Game(2);
        gui = new GUI(new menuListener(), new phase1Listener(), new phase2Listener());
    }

    // menu listeners
    public class menuListener implements ActionListener, MouseListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            switch (evt.getActionCommand()) {
                case "play":
                    gui.showPanel(GUI.PHASE1);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) e.getSource();
                switch (b.getName()) {
                    case "settings":
                        Config.debug("Settings clicked");
                        break;

                    default:
                        break;
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) e.getSource();
                b.setPressed(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) e.getSource();
                b.setPressed(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) e.getSource();
                b.setHovered(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) e.getSource();
                b.setHovered(false);
            }
        }
    }

    // phase 1 listener
    public class phase1Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            gui.showPanel(GUI.PHASE2);
        }
    }

    // phase 2 listener
    public class phase2Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            gui.showPanel(GUI.MENU);
        }
    }
}
