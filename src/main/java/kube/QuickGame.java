package kube;

import kube.model.Kube;
import kube.model.Game;
import kube.model.action.*;

import java.util.Scanner;

import kube.controller.CommandListener;
import kube.view.TextualMode;

public class QuickGame {

   

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Kube kube = new Kube();
        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();
        
        Game model = new Game(Game.local, kube, eventsToModel, eventsToView);
        TextualMode view = new TextualMode(model, eventsToView);
        CommandListener controller = new CommandListener(eventsToModel, eventsToView, scanner);

        Thread modelThread = new Thread(model);
        Thread controllerThread = new Thread(controller);
        Thread viewThread = new Thread(view);
        
        modelThread.start();
        viewThread.start();
        controllerThread.start();
    }




}               