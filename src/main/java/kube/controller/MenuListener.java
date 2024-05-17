package kube.controller;

import java.util.Scanner;

import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.model.ai.RandomAI;
import kube.services.Client;
import kube.services.Network;
import kube.services.Server;

public class MenuListener implements Runnable {

    private static final int PORT = 1234;
    Queue<Action> eventsToView;
    Queue<Action> eventsToModel;
    Queue<Action> eventsToController;
    Queue<Action> eventsToNetwork;
    Kube kube;
    Scanner scanner;

    public MenuListener(Kube kube, Queue<Action> eventsToView, Queue<Action> eventsToModel,
            Queue<Action> eventsToController, Scanner scanner) {
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.eventsToController = eventsToController;
        this.kube = kube;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        CommandListener controller = null;
        NetworkListener networkListener = null;
        NetworkSender networkSender = null;
        int mode, nb,type;
        mode = askGameMode();
        if (mode == 1) {
            controller = new CommandListener(eventsToModel, eventsToView, scanner);
            type = Game.local;
            nb = askNbPlayer();
            if (nb == 2) {
                kube.init();
            } else if (nb == 1) {
                kube.init(new RandomAI());
            } else if (nb == 0) {
                kube.init(new RandomAI(), new RandomAI());
            }

        } else {
            kube.init();
            mode = askHostOrJoin();
            Network network = null;
            if (mode == 1) {
                network = new Server(eventsToModel,PORT);
                type = Game.host;
            } else {
                network = askIP(eventsToModel);
                type = Game.join;
            }
            eventsToNetwork = new Queue<>();
            controller = new CommandListener(eventsToModel, eventsToView, eventsToNetwork, scanner);
            networkSender = new NetworkSender(network, eventsToNetwork);
            
            Thread networkListenerThread = new Thread(network);
            Thread networkSenderThread = new Thread(networkSender);
            networkSenderThread.start();
            networkListenerThread.start();
        }

        Game model = new Game(type, kube, eventsToModel, eventsToView, eventsToNetwork);
        Thread modelThread = new Thread(model);
        modelThread.start();

        Thread controllerThread = new Thread(controller);
        controllerThread.start();

    }

    public int askNbPlayer() {
        String s = "";
        eventsToView.add(new Action(Action.PRINT_ASK_NB_PLAYERS));
        s = scanner.nextLine();
        int i;
        try {
            if ((i = Integer.parseInt(s)) <= 2 && i >= 0) {
                return i;
            } else {
                return askNbPlayer();
            }
        } catch (NumberFormatException e) {
            return askNbPlayer();
        }
    }

    public int askGameMode() {
        String s = "";
        int i;
        eventsToView.add(new Action(Action.PRINT_ASK_GAME_MODE));
        s = scanner.nextLine();
        try {
            if ((i = Integer.parseInt(s)) <= 2 && i >= 1) {
                return i;
            } else {
                return askNbPlayer();
            }
        } catch (NumberFormatException e) {
            return askGameMode();
        }
    }

    private int askHostOrJoin() {
        String s = "";
        eventsToView.add(new Action(Action.PRINT_ASK_HOST_OR_JOIN));
        s = scanner.nextLine();
        int i;
        try {
            if ((i = Integer.parseInt(s)) <= 2 && i >= 1) {
                return i;
            } else {
                return askNbPlayer();
            }
        } catch (NumberFormatException e) {
            return askHostOrJoin();
        }
    }

    private Network askIP(Queue<Action> networkToModel) {
        String s = "";
        eventsToView.add(new Action(Action.PRINT_ASK_IP));
        s = scanner.nextLine();
        Network network = new Client(networkToModel, s,PORT);
        eventsToView.add(new Action(Action.PRINT_START));
        return network;
    }
}