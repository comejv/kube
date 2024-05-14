package kube.model;

import kube.model.ai.*;

import java.util.Random;

import kube.configuration.Config;
import kube.model.action.*;
import kube.model.action.move.Move;

public class Game implements Runnable {
    public static final int localTextual = 1;
    public static final int onlineServerTextual = 2;
    public static final int onlineClientTextual = 3;

    Queue<Action> events;
    private int gameType;
    private Kube k3;
    Player currentPlayerToBuild;

    public Game(int gameType, Kube k3, Queue<Action> events) {
        this.gameType = gameType;
        this.k3 = k3;
        this.events = events;
    }

    @Override
    public void run() {
        switch (gameType) {
            case localTextual:
                localTextualGame();
                break;
            default:
                break;
        }
    }

    public void localTextualGame() {
        Config.debug("Démarrage de la partie locale");
        // Initialisation
        k3.init();
        currentPlayerToBuild = k3.getP1();

        // Construction phase
        while (k3.getPhase() == 1) {
            if (currentPlayerToBuild.isAI()) {
                currentPlayerToBuild.getAI().constructionPhase();
            } else {
                Action a = events.remove();
                switch (a.getType()) {
                    case Action.SWAP:
                        swap((Swap) a.getData());
                        break;
                    case Action.VALIDATE:
                        if (currentPlayerToBuild.validateBuilding()) {
                            Config.debug("Validation construction j1");
                            currentPlayerToBuild = k3.getP2();
                        }
                        k3.updatePhase();
                        break;
                    case Action.SHUFFLE:
                        utilsAI.randomFillMountain(currentPlayerToBuild, new Random());
                        break;
                    default:
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
                Action a = events.remove();
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
}
