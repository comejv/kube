package kube.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kube.view.GUI;

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
    public class menuListener implements ActionListener {
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
    }

    // phase 1 listener
    public class phase1Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            switch (evt.getActionCommand()) {
                case "phase2":
                    gui.showPanel(GUI.PHASE2);
                    break;
                case "menu": //change to opts.
                    gui.showPanel(GUI.MENU);

                default:
                    break;
            }
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
