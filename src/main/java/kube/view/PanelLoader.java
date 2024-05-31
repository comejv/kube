package kube.view;

import javax.swing.JPanel;

import kube.configuration.Config;
import kube.controller.graphical.GUIControllers;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.panels.FirstPhasePanel;
import kube.view.panels.SecondPhasePanel;

public class PanelLoader implements Runnable {
    // TODO : refactor this class to make it more readable
    GUI gui;
    String panelName;
    GUIControllers controllers;
    Kube k3;
    Queue<Action> eventsToView;
    Queue<Action> eventsToModel;
    
    PanelLoader(GUI gui, String panelName, Kube k3, GUIControllers controller, Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.gui = gui;
        this.panelName = panelName;
        this.controllers = controller;
        this.k3 = k3;
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
    }

    public void run() {
        JPanel panel;
        switch (panelName) {
            case GUI.PHASE1:
                panel = new FirstPhasePanel(gui, k3, controllers.getPhase1Controller(), eventsToView, eventsToModel);
                break;
            case GUI.PHASE2:
                panel = new SecondPhasePanel(gui, k3, controllers.getPhase2Controller());
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
