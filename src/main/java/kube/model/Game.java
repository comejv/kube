package kube.model;

import kube.model.ai.*;

import java.util.Random;
import java.util.concurrent.ExecutionException;

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
        k3.init(new RandomAI(), new RandomAI());
        currentPlayerToBuild = k3.getP1();
        // Construction phase
        while (k3.getPhase() == 1) {
            try {

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
                            modeleToView.add(a);
                            break;
                        case Action.VALIDATE:
                            boolean isValidated;
                            if ((isValidated = currentPlayerToBuild.validateBuilding())) {
                                Config.debug("Validation construction j" + currentPlayerToBuild.getId());
                                currentPlayerToBuild = k3.getP2();
                            }
                            k3.updatePhase();
                            a.setData(isValidated);
                            modeleToView.add(a);
                            break;
                        case Action.SHUFFLE:
                            utilsAI.randomFillMountain(currentPlayerToBuild, new Random());
                            modeleToView.add(a);
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

        Action redirectAction;
        while (k3.canCurrentPlayerPlay()) {
            try {
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
                            Move move = k3.moveSet().get((int) a.getData());
                            k3.playMove(move);
                            modeleToView.add(new Action(Action.MOVE, move));
                            break;
                        case Action.UNDO:
                            k3.unPlay();
                            redirectAction = new Action(Action.UNDO, k3.getLastMovePlayed());
                            modeleToView.add(redirectAction);
                            break;
                        case Action.REDO:
                            k3.rePlay();
                            redirectAction = new Action(Action.REDO, k3.getLastMovePlayed());
                            modeleToView.add(redirectAction);
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
        modeleToView.add(new Action(Action.PRINT_WIN_MESSAGE, k3.getCurrentPlayer()));
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
}
