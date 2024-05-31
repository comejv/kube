package kube;

import java.util.Scanner;

import kube.controller.textual.CommandListener;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.TextualMode;

public class QuickGame {
    // TODO : refactor this class to make it more readable

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Kube kube = new Kube();
        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();
        Queue<Action> eventsToNetwork = new Queue<>();

        Game model = new Game(Game.LOCAL, kube, eventsToModel, eventsToView, eventsToNetwork);
        TextualMode view = new TextualMode(kube, eventsToView);
        CommandListener controller = new CommandListener(eventsToModel, eventsToView, scanner);

        Thread modelThread = new Thread(model);
        Thread controllerThread = new Thread(controller);
        Thread viewThread = new Thread(view);

        modelThread.start();
        viewThread.start();
        controllerThread.start();
    }

}
