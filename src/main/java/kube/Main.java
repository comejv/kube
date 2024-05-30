package kube;

import kube.configuration.ResourceLoader;
import kube.controller.graphical.GUIControllers;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.GUI;

public class Main {
    public static void main(String[] args) {
        new ResourceLoader();
        Kube kube = new Kube();
        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();
        Queue<Action> eventsToNetwork = new Queue<>();

        GUIControllers controllers = new GUIControllers(kube,eventsToView, eventsToModel, eventsToNetwork);
        new GUI(kube, controllers, eventsToView, eventsToModel);
    }
}
