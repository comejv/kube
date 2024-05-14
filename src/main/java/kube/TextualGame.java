package kube;

import kube.model.Kube;
import kube.model.Game;
import kube.model.action.*;

import kube.controller.CommandListener;
import kube.view.TextualMode;

public class TextualGame {
    public static void main(String[] args) {
        Kube kube = new Kube();
        Queue<Action> controllerToModel = new Queue<>();
        Queue<Action> modelToView = new Queue<>();
        Game model = new Game(Game.local, kube, controllerToModel, modelToView);
        TextualMode view = new TextualMode(model, modelToView);
        CommandListener controller = new CommandListener(controllerToModel, view);

        Thread modelThread = new Thread(model);
        Thread controllerThread = new Thread(controller);
        Thread viewThread = new Thread(view);

        modelThread.start();
        controllerThread.start();
        viewThread.start();


        
    }
}               