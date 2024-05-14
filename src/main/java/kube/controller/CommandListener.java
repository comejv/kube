package kube.controller;


import java.util.ArrayList;
import java.util.Scanner;

import kube.model.action.*;
import kube.model.action.move.Move;
import kube.view.TextualMode;

public class CommandListener implements Runnable {

    String command;
    TextualMode tm;
    Queue<Action> actions;

    public CommandListener(Queue<Action> actions, TextualMode tm) {
        this.actions = actions;
        this.tm = tm;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            switch (sc.nextLine()) {
                case "random":
                    actions.add(new Action(Action.SHUFFLE));
                    break;
                case "echanger":
                    swap(sc);
                    break;
                case "afficher":
                    tm.printState();
                    break;
                case "valider":
                    actions.add(new Action(Action.VALIDATE));
                    break;
                case "jouer":
                    playMove(sc);
                    break;
                case "annuler":
                    actions.add(new Action(Action.UNDO));
                    break;
                case "rejouer":
                    actions.add(new Action(Action.REDO));
                    break;
                case "":
                    break;
                default:
                    System.out.println("Commande inconnue");
                    break;
            }
            System.out.println("\n");
            tm.update();
        }
    }

    public boolean swap(Scanner sc) {
        try {
            tm.printWaitCoordinates(1);
            String s = sc.nextLine();
            String[] coords = s.split(" ");
            int x1 = Integer.parseInt(coords[0]);
            int y1 = Integer.parseInt(coords[1]);
            tm.printWaitCoordinates(2);
            s = sc.nextLine();
            coords = s.split(" ");
            int x2 = Integer.parseInt(coords[0]);
            int y2 = Integer.parseInt(coords[1]);
            actions.add(new Action(Action.SWAP,new Swap(x1, y1, x2, y2)));
            tm.printSwapSuccess(x1, y1, x2, y2);
            return true;
        } catch (Exception e) {
            tm.printSwapError();
            return false;
        }
    }

    public boolean playMove(Scanner sc) {
        ArrayList<Move> moves = tm.printListMoves();
        String s = sc.nextLine();
        try {
            int n = Integer.parseInt(s);
            actions.add(new Action(Action.MOVE,moves.get(n)));
            tm.printMove(moves.get(n));
            return true;
        } catch (Exception e) {
            tm.printMoveError();
            return false; 
        }
    }
}
