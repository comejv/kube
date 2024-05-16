package kube.controller;

import java.util.Scanner;

import kube.model.action.*;

public class CommandListener implements Runnable {

    String command;
    Queue<Action> eventsToModel;
    Queue<Action> eventsToView;
    Scanner sc;

    public CommandListener(Queue<Action> eventsToModel, Queue<Action> eventsToView, Scanner sc) {
        this.eventsToModel = eventsToModel;
        this.eventsToView = eventsToView;
        this.sc = sc;
    }

    @Override
    public void run() {
        /*
        // Add the amount of players sector
        */
        while (sc.hasNextLine()) {
            switch (sc.nextLine()) {
                case "random":
                    eventsToModel.add(new Action(Action.SHUFFLE));
                    break;
                case "echanger":
                    swap(sc);
                    break;
                case "afficher":
                    eventsToView.add(new Action(Action.PRINT_STATE));
                    break;
                case "valider":
                    eventsToModel.add(new Action(Action.VALIDATE));
                    break;
                case "jouer":
                    playMove(sc);
                    break;
                case "annuler":
                    eventsToModel.add(new Action(Action.UNDO));
                    break;
                case "rejouer":
                    eventsToModel.add(new Action(Action.REDO));
                    break;
                case "aide":
                case "":
                    eventsToView.add(new Action(Action.PRINT_HELP));
                    break;
                default:
                    eventsToView.add(new Action(Action.PRINT_COMMAND_ERROR));
                    break;
            }
        }
    }

    public boolean swap(Scanner sc) {
        try {
            eventsToView.add(new Action(Action.PRINT_WAIT_COORDINATES, 1));
            String s = sc.nextLine();
            String[] coords = s.split(" ");
            int x1 = Integer.parseInt(coords[0]);
            int y1 = Integer.parseInt(coords[1]);
            eventsToView.add(new Action(Action.PRINT_WAIT_COORDINATES,2));
            s = sc.nextLine();
            coords = s.split(" ");
            int x2 = Integer.parseInt(coords[0]);
            int y2 = Integer.parseInt(coords[1]);
            Swap swap = new Swap(x1, y1, x2, y2);
            eventsToModel.add(new Action(Action.SWAP, swap));
            eventsToView.add(new Action(Action.PRINT_SWAP, swap));
            return true;
        } catch (Exception e) {
            eventsToView.add(new Action(Action.PRINT_SWAP_ERROR));
            return false;
        }
    }

    public boolean playMove(Scanner sc) {
        eventsToView.add(new Action(Action.PRINT_LIST_MOVES));
        String s = sc.nextLine();
        try {
            int n = Integer.parseInt(s);
            eventsToModel.add(new Action(Action.MOVE,n));
            return true;
        } catch (Exception e) {
            eventsToView.add(new Action(Action.PRINT_MOVE_ERROR));
            return false;
        }
    }
}