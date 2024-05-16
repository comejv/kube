package kube.model;

import kube.model.ai.*;

import java.util.Random;


import kube.configuration.Config;
import kube.model.action.*;
import kube.model.action.move.Move;

public class Game implements Runnable {
    public static final int local = 1;

    Queue<Action> controllerToModele;
    Queue<Action> modeleToView;
    private int gameType;
    private Kube k3;

    public Game(int gameType, Kube k3, Queue<Action> controllerToModele, Queue<Action> modeleToView) {
        this.gameType = gameType;
        this.k3 = k3;
        this.controllerToModele = controllerToModele;
        this.modeleToView = modeleToView;
    }

    @Override
    public void run() {
        switch (gameType) {
            case local:
                localGame();
                break;
            default:
                break;
        }
    }

    public void localGame() {
        Config.debug("Démarrage de la partie locale");
        // Initialisation
        k3.init();
        // Construction phase
        while (k3.getPhase() == 1) {
            try {
                if (k3.getCurrentPlayer().isAI()) {
                    k3.getCurrentPlayer().getAI().constructionPhase();
                    k3.updatePhase();
                } else {
                    Action a = controllerToModele.remove();
                    switch (a.getType()) {
                        case Action.SWAP:
                            swap((Swap) a.getData());
                            modeleToView.add(a);
                            break;
                        case Action.VALIDATE:
                            boolean isValidated;
                            if ((isValidated = k3.getCurrentPlayer().validateBuilding())) {
                                Config.debug("Validation construction j" + k3.getCurrentPlayer().getId());
                            }
                            k3.updatePhase();
                            modeleToView.add(new Action(Action.PRINT_VALIDATE, isValidated));
                            break;
                        case Action.SHUFFLE:
                            utilsAI.randomFillMountain(k3.getCurrentPlayer(), new Random());
                            modeleToView.add(new Action(Action.PRINT_RANDOM));
                            break;
                        default:
                            modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
                            break;
                    }
                }
            } catch (Exception e) {
                modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
            }
        }

        Config.debug("Fin phase 1");
        // Game phase

        while (k3.canCurrentPlayerPlay()) {
            try {
                if (k3.getCurrentPlayer().isAI()) {
                    try {
                        k3.playMove(k3.getCurrentPlayer().getAI().nextMove());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                } else {
                    Action a = controllerToModele.remove();
                    switch (a.getType()) {
                        case Action.MOVE:
                            playMove(a);
                            break;
                        case Action.UNDO:
                            undo();
                            break;
                        case Action.REDO:
                            redo();
                            break;
                        default:
                            modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
                            break;
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
                modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
            }

        }
        if (k3.getCurrentPlayer() == k3.getP1()) {
            modeleToView.add(new Action(Action.PRINT_WIN_MESSAGE, k3.getP2()));
        } else {
            modeleToView.add(new Action(Action.PRINT_WIN_MESSAGE, k3.getP1()));
        }
        Config.debug("Fin phase 2");
    }

    synchronized public Kube getKube() {
        return k3;
    }

    synchronized public void swap(Swap s) {
        Color c = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos1().x, s.getPos1().y);
        Color c2 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos2().x, s.getPos2().y);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos1(), c2);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos2(), c);
    }

    public void playMove(Action a) {
        try {
            Move move = k3.moveSet().get((int) a.getData());
            if (k3.playMove(move)) {
                modeleToView.add(new Action(Action.PRINT_MOVE, move));
            } else {
                modeleToView.add(new Action(Action.PRINT_MOVE_ERROR, move));
            }
        } catch (Exception e) {
            modeleToView.add(new Action(Action.PRINT_MOVE_ERROR));
        }
    }

    public void undo() {
        if (k3.unPlay()) {
            modeleToView.add(new Action(Action.PRINT_UNDO, k3.getLastMovePlayed()));
        } else {
            modeleToView.add(new Action(Action.PRINT_UNDO_ERROR));
        }
    }

    public void redo() {
        if (k3.rePlay()) {
            modeleToView.add(new Action(Action.PRINT_REDO, k3.getLastMovePlayed()));
        } else {
            modeleToView.add(new Action(Action.PRINT_REDO_ERROR));
        }
    }
}