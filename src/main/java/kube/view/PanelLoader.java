package kube.view;

import javax.swing.JPanel;

import kube.controller.graphical.GUIControllers;
import kube.model.Game;
import kube.view.panels.FirstPhasePanel;
import kube.view.panels.SecondPhasePanel;

public class PanelLoader implements Runnable {
    GUI gui;
    String panelName;
    GUIControllers controllers;
    Game model;

    PanelLoader(GUI gui, String panelName, Game model, GUIControllers controller) {
        this.gui = gui;
        this.panelName = panelName;
        this.controllers = controller;
        this.model = model;
    }

    public void run() {
        JPanel panel;
        switch (panelName) {
            case GUI.PHASE1:
                panel = new FirstPhasePanel(gui, model, controllers.getPhase1Controller());
                break;
            case GUI.PHASE2:
                panel = new SecondPhasePanel(gui, model, controllers.getPhase2Controller());
                break;
            default:
                panel = null;
                break;
        }
        gui.setPanel(panelName, panel);
        synchronized (gui) {
            gui.notify();
        }
    }
}
