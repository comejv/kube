package kube.controller.textual;

import java.util.Scanner;

import kube.controller.network.NetworkListener;
import kube.controller.network.NetworkSender;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.model.ai.moveSetHeuristique;
import kube.model.ai.randomAI;
import kube.services.Client;
import kube.services.Network;
import kube.services.Server;

public class MenuListener implements Runnable {

    private static final int PORT = 1234;
    Queue<Action> eventsToView;
    Queue<Action> eventsToModel;
    
    Queue<Action> eventsToNetwork;
    Kube kube;
    Scanner scanner;

    public MenuListener(Kube kube, Queue<Action> eventsToView, Queue<Action> eventsToModel, Scanner scanner) {
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        
        this.kube = kube;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        CommandListener controller;
        NetworkListener networkListener;
        NetworkSender networkSender;
        int mode, nb, type;
        mode = askGameMode();
        if (mode == 1) {
            controller = new CommandListener(eventsToModel, eventsToView, scanner);
            type = Game.LOCAL;
            nb = askNbPlayer();
            switch (nb) {
                case 0:
                    kube.init(new moveSetHeuristique(), new randomAI());
                    break;
                case 1:
                    kube.init(new randomAI());
                    break;
                case 2:
                    kube.init();
                    break;

                default:
                    break;
            }

        } else {
            kube.init(new moveSetHeuristique(300));
            mode = askHostOrJoin();
            Network network;
            if (mode == 1) {
                eventsToView.add(new Action(ActionType.PRINT_WAITING_FOR_CONNECTION,(Integer) PORT));
                network = new Server(PORT);
                eventsToView.add(new Action(ActionType.PRINT_CONNECTION_ETABLISHED));
                type = Game.HOST;
            } else {
                network = askIP();
                eventsToView.add(new Action(ActionType.PRINT_CONNECTION_ETABLISHED));
                type = Game.JOIN;
            }
            eventsToNetwork = new Queue<>();
            controller = new CommandListener(eventsToModel, eventsToView, eventsToNetwork, type, scanner);
            networkSender = new NetworkSender(network, eventsToNetwork,type);
            networkListener = new NetworkListener(network, eventsToModel);

            Thread networkListenerThread = new Thread(networkListener);
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
        String s;
                eventsToView.add(new Action(ActionType.PRINT_ASK_NB_PLAYERS));
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
        String s;
        int i;
        eventsToView.add(new Action(ActionType.PRINT_ASK_GAME_MODE));
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
        String s;
        eventsToView.add(new Action(ActionType.PRINT_ASK_HOST_OR_JOIN));
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

    private Network askIP() {
        String s;
        Network network = new Client();
        eventsToView.add(new Action(ActionType.PRINT_ASK_IP));
        s = scanner.nextLine();
        if (!network.connect(s, PORT)) {
            eventsToView.add(new Action(ActionType.PRINT_CONNECTION_ERROR));
            return askIP();
        } else {
            eventsToView.add(new Action(ActionType.PRINT_START));
            return network;
        }
    }
}
