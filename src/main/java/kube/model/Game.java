package kube.model;

import java.awt.Desktop;
import java.io.ObjectInputFilter;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.model.action.Swap;
import kube.model.action.move.Move;
import kube.model.ai.moveSetHeuristique;
import kube.model.ai.utilsAI;

public class Game implements Runnable {

    public static final int LOCAL = 0;
    public static final int HOST = 1;
    public static final int JOIN = 2;

    public static final int PORT = 1234;

    Queue<Action> controllerToModele;
    Queue<Action> modeleToView;
    Queue<Action> eventsToNetwork;
    Queue<Action> eventsToModele;
    private int gameType;
    private final Kube k3;

    public Game(int gameType, Kube k3, Queue<Action> controllerToModele, Queue<Action> modeleToView,
            Queue<Action> eventsToNetwork) {
        this.gameType = gameType;
        this.k3 = k3;
        this.controllerToModele = controllerToModele;
        this.eventsToModele = controllerToModele;
        this.eventsToNetwork = eventsToNetwork;
        this.modeleToView = modeleToView;
    }

    @Override
    public void run() {
        localGame(gameType);
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getGameType() {
        return gameType;
    }

    public void constructionPhase() {
        while (k3.getPhase() == 1) {
            if (k3.getCurrentPlayer().isAI()) {
                constructionPhaseIA(k3.getCurrentPlayer());
            } else {
                if (getGameType() == LOCAL){
                    constructionPhasePlayer(k3.getP1());
                    constructionPhasePlayer(k3.getP2());
                    k3.setCurrentPlayer(k3.getRandomPlayer());
                } else if (getGameType() == HOST){
                    constructionPhasePlayer(k3.getP1());
                    waitConstruction(k3.getP2());
                    k3.setCurrentPlayer(k3.getRandomPlayer());
                } else {
                    constructionPhasePlayer(k3.getP2());
                    waitConstruction(k3.getP1());
                }
            }
        }
    }

    public void constructionPhaseIA(Player p) {
        k3.getCurrentPlayer().getAI().constructionPhase();
        modeleToView.add(new Action(Action.VALIDATE, true));
        k3.updatePhase();
    }

    public void waitConstruction(Player p){
        modeleToView.add(new Action(Action.PRINT_NOT_YOUR_TURN));
        while (k3.getPhase() == Kube.PREPARATION_PHASE){
            Action a = eventsToModele.remove();
            if (a.getType() == Action.VALIDATE && a.getPlayer() == p.getId()){
                if (getGameType() == HOST) {
                    k3.setP2((Player) a.getData());
                } else {
                    k3.setP1((Player) a.getData());
                }
                k3.updatePhase();
            }
            else {
                modeleToView.add(new Action(Action.PRINT_NOT_YOUR_TURN));
            }
        }
        modeleToView.add(new Action(Action.ITS_YOUR_TURN));
    }

    public void constructionPhasePlayer(Player p) {
        while (!p.getHasValidateBuilding()) {
            Action a = eventsToModele.remove();
            switch (a.getType()) {
                case Action.SWAP:
                    swap((Swap) a.getData());
                    modeleToView.add(a);
                    break;
                case Action.SHUFFLE:
                    utilsAI.randomFillMountain(k3.getCurrentPlayer(), new Random());
                    modeleToView.add(new Action(Action.SHUFFLE));
                    break;
                case Action.VALIDATE:
                    // Reception of the other player mountain
                    if (getGameType() != LOCAL && a.getPlayer() != getGameType()) {
                        if (getGameType() == JOIN) {
                            k3.setP1((Player) a.getData());
                        } else {
                            k3.setP2((Player) a.getData());
                        }
                    } else {
                        boolean isValidated = k3.getCurrentPlayer().validateBuilding();
                        if (isValidated && getGameType() != LOCAL) {
                            Action ac;
                            if (getGameType() == HOST){
                                ac = new Action(Action.VALIDATE, k3.getP1().clone());
                            } else {
                                ac = new Action(Action.VALIDATE, k3.getP2().clone());
                            }
                            eventsToNetwork.add(ac);
                        }
                        k3.updatePhase();
                        modeleToView.add(new Action(Action.VALIDATE, isValidated));
                    }
                    break;
                default:
                    modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
                    break;
            }
        }
    }

    public void gamePhase() {
        while (k3.canCurrentPlayerPlay()) {
            if (k3.getCurrentPlayer().isAI()) {
                Move move = k3.getCurrentPlayer().getAI().nextMove();
                k3.playMove(move);
                modeleToView.add(new Action(Action.MOVE, move));
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
        }
    }

    public void initPhase(){
        if (getGameType() == HOST) {
            eventsToNetwork.add(new Action(Action.INIT_K3, k3.getK3()));
            eventsToNetwork.add(new Action(Action.PLAYER_DATA, k3.getP1()));
            eventsToNetwork.add(new Action(Action.PLAYER_DATA, k3.getP2()));
            k3.setCurrentPlayer(k3.getP1());
        } else if (getGameType() == JOIN) {
            Action a;
            while ((a = eventsToModele.remove()).getType() != Action.INIT_K3){
                modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
            }
            k3.setK3((Mountain) a.getData());
            while ((a = eventsToModele.remove()).getType() != Action.PLAYER_DATA){
                modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
            }
            k3.setP1((Player) a.getData());
            while ((a = eventsToModele.remove()).getType() != Action.PLAYER_DATA){
                modeleToView.add(new Action(Action.PRINT_FORBIDDEN_ACTION));
            }
            k3.setP2((Player) a.getData());
            k3.setCurrentPlayer(k3.getP2());
        } else {
            k3.setCurrentPlayer(k3.getP1());
        }
    }


    public void localGame(int type) {
        initPhase();
        constructionPhase();
        gamePhase();


        if (k3.getCurrentPlayer() == k3.getP1()) {
            modeleToView.add(new Action(Action.PRINT_WIN_MESSAGE, k3.getP2()));
        } else {
            modeleToView.add(new Action(Action.PRINT_WIN_MESSAGE, k3.getP1()));
        }
        Config.debug("Fin phase 2");
    }

    synchronized public void swap(Swap s) {
        ModelColor c = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos1().x, s.getPos1().y);
        ModelColor c2 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos2().x, s.getPos2().y);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos1(), c2);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos2(), c);
    }

    public void playMove(Action a) {
        try {
            Move move = k3.moveSet().get((int) a.getData());
            if (k3.playMove(move)) {
                modeleToView.add(new Action(Action.MOVE, move));
            } else {
                modeleToView.add(new Action(Action.MOVE, move));
            }
        } catch (UnsupportedOperationException e) {
            modeleToView.add(new Action(Action.MOVE, null));
        }

    }

    public void undo() {
        if (k3.getHistory().canUndo() && k3.unPlay()) {
            while (k3.getCurrentPlayer().isAI() && k3.getHistory().canUndo()) {
                k3.unPlay();
            }
            modeleToView.add(new Action(Action.UNDO, k3.getLastMovePlayed()));
        } else {
            modeleToView.add(new Action(Action.UNDO, null));
        }
    }

    public void redo() {
        if (k3.getHistory().canRedo() && k3.rePlay()) {
            modeleToView.add(new Action(Action.REDO, k3.getLastMovePlayed()));
        } else {
            modeleToView.add(new Action(Action.REDO, null));
        }
    }

}
