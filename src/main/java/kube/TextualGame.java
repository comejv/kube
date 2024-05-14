package kube;

import kube.model.Kube;
import kube.model.Game;
import kube.model.action.*;

import kube.controller.CommandListener;
import kube.view.TextualMode;

public class TextualGame {
    public static void main(String[] args) {
        Kube kube = new Kube();
        Queue<Action> events = new Queue<>();
        Game model = new Game(Game.localTextual, kube, events);
        TextualMode view = new TextualMode(model);
        CommandListener controller = new CommandListener(events, view);

        Thread modelThread = new Thread(model);
        Thread controllerThread = new Thread(controller);
        
        modelThread.start();
        controllerThread.start();


        
    }
}               