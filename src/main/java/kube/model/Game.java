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
    Player currentPlayerToBuild;

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
        k3.init(new RandomAI());
        currentPlayerToBuild = k3.getP1();

        // Construction phase
        while (k3.getPhase() == 1) {
            if (currentPlayerToBuild.isAI()) {
                currentPlayerToBuild.getAI().constructionPhase();
                if (currentPlayerToBuild.validateBuilding()) {
                    Config.debug("Validation construction IA");
                    currentPlayerToBuild = k3.getP2();
                }
                k3.updatePhase();
            } else {
                Action a = controllerToModele.remove();
                switch (a.getType()) {
                    case Action.SWAP:
                        swap((Swap) a.getData());
                        break;
                    case Action.VALIDATE:
                        if (currentPlayerToBuild.validateBuilding()) {
                            Config.debug("Validation construction j" + currentPlayerToBuild.getId());
                            currentPlayerToBuild = k3.getP2();
                        }
                        k3.updatePhase();
                        break;
                    case Action.SHUFFLE:
                        utilsAI.randomFillMountain(currentPlayerToBuild, new Random());
                        break;
                    default:
                        redirectMessage(a);
                        break;
                }
            }
        }

        Config.debug("Fin phase 1");
        // Game phase

        while (k3.canCurrentPlayerPlay()) {
            if (k3.getCurrentPlayer().isAI()) {
                try {
                    k3.playMove(k3.getCurrentPlayer().getAI().nextMove());
                } catch (Exception e) {
                    System.err.println("Coup de l'IA impossible" + e);
                }
            } else {
                Action a = controllerToModele.remove();
                switch (a.getType()) {
                    case Action.MOVE:
                        k3.playMove((Move) a.getData());
                        break;
                    case Action.UNDO:
                        k3.unPlay();
                        break;
                    case Action.REDO:
                        k3.rePlay();
                        break;
                    default:
                        redirectMessage(a);
                        break;
                }
            }
        }
        Config.debug("Fin phase 2");
    }

    synchronized public Player getCurrentPlayerToBuild() {
        return currentPlayerToBuild;
    }

    synchronized public Kube getKube() {
        return k3;
    }

    synchronized public void swap(Swap s) {
        Color c = currentPlayerToBuild.unbuildFromMoutain(s.getPos1().x, s.getPos1().y);
        Color c2 = currentPlayerToBuild.unbuildFromMoutain(s.getPos2().x, s.getPos2().y);
        currentPlayerToBuild.buildToMoutain(s.getPos1(), c2);
        currentPlayerToBuild.buildToMoutain(s.getPos2(), c);
    }

    public void redirectMessage(Action a) {
        switch (a.getType()) {
            case Action.PRINT_AI:
            case Action.PRINT_COMMAND_ERROR:
            case Action.PRINT_WAIT_COORDINATES:
            case Action.PRINT_GOODBYE:
            case Action.PRINT_HELP:
            case Action.PRINT_LIST_MOVES:
            case Action.PRINT_MOVE:
            case Action.PRINT_MOVE_ERROR:
            case Action.PRINT_NEXT_PLAYER:
            case Action.PRINT_PLAYER:
            case Action.PRINT_PLAYER_NAME:
            case Action.PRINT_RANDOM:
            case Action.PRINT_REDO:
            case Action.PRINT_REDO_ERROR:
            case Action.PRINT_START:
            case Action.PRINT_STATE:
            case Action.PRINT_SWAP:
            case Action.PRINT_SWAP_ERROR:
            case Action.PRINT_SWAP_SUCCESS:
            case Action.PRINT_UNDO:
            case Action.PRINT_UNDO_ERROR:
            case Action.PRINT_VALIDATE:
            case Action.PRINT_WELCOME:
            case Action.PRINT_WIN_MESSAGE:
            case Action.UPDATE:
                modeleToView.add(a);
            default:
                break;
        }
    }
}
