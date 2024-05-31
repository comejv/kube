package kube;

import java.util.Scanner;

import kube.controller.textual.MenuListener;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.TextualMode;

public class TextualGame {
    // TODO : refactor this class to make it more readable

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Kube kube = new Kube();

        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();

        MenuListener menuListener = new MenuListener(kube, eventsToView, eventsToModel, scanner);
        TextualMode view = new TextualMode(kube, eventsToView);

        Thread viewThread = new Thread(view);
        Thread menuThread = new Thread(menuListener);
        
        viewThread.start();
        menuThread.start();
    }
}
