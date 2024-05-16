package kube;

import java.util.Scanner;

import kube.model.Kube;
import kube.model.action.*;

import kube.controller.MenuListener;
import kube.view.TextualMode;

public class TextualGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Kube kube = new Kube();

        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();
        Queue<Action> eventsToController = new Queue<>();

        MenuListener menuListener = new MenuListener(kube, eventsToView, eventsToModel, eventsToController, scanner);
        TextualMode view = new TextualMode(kube, eventsToView);

        Thread viewThread = new Thread(view);
        Thread menuThread = new Thread(menuListener);
        viewThread.start();
        menuThread.start();
    }

}