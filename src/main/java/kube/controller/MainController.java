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
        gui = GUI.start(new menuListener(), new phase1Listener(), new phase2Listener());
    }

    // menu listeners
    public class menuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            // Si changement frame : mF.showPanel(gui.PHASE1)
        }
    }

    // phase 1 listener
    public class phase1Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {

        }
    }

    // phase 2 listener
    public class phase2Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {

        }
    }
}
