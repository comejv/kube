package kube.controller.textual;

import java.util.Scanner;

import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.model.action.Swap;

public class CommandListener implements Runnable {

    Queue<Action> eventsToModel;
    Queue<Action> eventsToView;
    Queue<Action> eventsToNetwork;
    int whoAmI;
    Scanner sc;

    public CommandListener(Queue<Action> eventsToModel, Queue<Action> eventsToView, Scanner sc) {
        this.eventsToModel = eventsToModel;
        this.eventsToView = eventsToView;
        this.sc = sc;

    }

    public CommandListener(Queue<Action> eventsToModel, Queue<Action> eventsToView, Queue<Action> eventsToNetwork,
            int whoAmI,
            Scanner sc) {
        this(eventsToModel, eventsToView, sc);
        this.eventsToNetwork = eventsToNetwork;
        this.whoAmI = whoAmI;

    }

    @Override
    public void run() {
        while (sc.hasNextLine()) {
            switch (sc.nextLine()) {
                case "random":
                case "shuffle":
                    eventsToModel(new Action(ActionType.SHUFFLE));
                    break;
                case "echanger":
                case "swap":
                    swap(sc);
                    break;
                case "afficher":
                case "print":
                case "display":
                    eventsToView.add(new Action(ActionType.PRINT_STATE));
                    break;
                case "valider":
                case "validate":
                    eventsToModel(new Action(ActionType.VALIDATE));
                    break;
                case "jouer":
                case "play":
                    playMove(sc);
                    break;
                case "annuler":
                case "cancel":
                case "undo":
                    eventsToModel(new Action(ActionType.UNDO));
                    break;
                case "rejouer":
                case "replay":
                case "redo":
                    eventsToModel(new Action(ActionType.REDO));
                    break;
                case "sauvegarder":
                case "save":
                case "enregistrer":
                case "serialize":
                    save(sc);
                    break;
                case "restaurer":
                case "charger":
                case "restore":
                case "load":
                    load(sc);
                    break;
                case "aide":
                case "help":
                case "":
                    eventsToView.add(new Action(ActionType.PRINT_HELP));
                    break;
                case "start":
                    eventsToModel(new Action(ActionType.START));
                    break;
                default:
                    eventsToView.add(new Action(ActionType.PRINT_COMMAND_ERROR));
                    break;
            }
        }
    }

    private boolean swap(Scanner sc) {
        try {
            eventsToView.add(new Action(ActionType.PRINT_WAIT_COORDINATES, 1));
            String s = sc.nextLine();
            String[] coords = s.split(" ");
            int x1 = Integer.parseInt(coords[0]);
            int y1 = Integer.parseInt(coords[1]);
            eventsToView.add(new Action(ActionType.PRINT_WAIT_COORDINATES, 2));
            s = sc.nextLine();
            coords = s.split(" ");
            int x2 = Integer.parseInt(coords[0]);
            int y2 = Integer.parseInt(coords[1]);
            Swap swap = new Swap(x1, y1, x2, y2);
            eventsToModel.add(new Action(ActionType.SWAP, swap));
            eventsToView.add(new Action(ActionType.SWAP, swap));
            return true;
        } catch (NumberFormatException e) {
            eventsToView.add(new Action(ActionType.SWAP));
            return false;
        }
    }

    private boolean playMove(Scanner sc) {
        eventsToView.add(new Action(ActionType.PRINT_LIST_MOVES));
        String s = sc.nextLine();
        try {
            int n = Integer.parseInt(s);
            eventsToModel(new Action(ActionType.MOVE_NUMBER, (Integer) n));
            return true;
        } catch (NumberFormatException e) {
            eventsToView.add(new Action(ActionType.MOVE));
            return false;
        }
    }

    private void save(Scanner sc) {
        eventsToView.add(new Action(ActionType.PRINT_ASK_SAVE_FILE_NAME));
        String s = sc.nextLine();
        eventsToModel(new Action(ActionType.SAVE, s));
        eventsToView.add(new Action(ActionType.PRINT_SAVED));
    }

    private void load(Scanner sc) {
        eventsToModel(new Action(ActionType.RESET));
        eventsToView.add(new Action(ActionType.PRINT_ASK_LOAD_FILE_NAME));
        String s = sc.nextLine();
        eventsToModel(new Action(ActionType.LOAD, s));
        eventsToView.add(new Action(ActionType.PRINT_LOADED));
    }

    private void eventsToModel(Action action) {
        if (eventsToNetwork != null) {
            action.setPlayer(whoAmI);
            eventsToModel.add(action);
        } else {
            eventsToModel.add(action);
        }
    }
}
