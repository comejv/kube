package kube.model;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.model.action.Swap;
import kube.model.action.move.Move;
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

    public void initPhase() {
        switch (getGameType()) {
            case HOST:
                eventsToNetwork.add(new Action(ActionType.INIT_K3, k3.getK3().clone()));
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getP1().clone()));
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getP2().clone()));
                k3.setCurrentPlayer(k3.getP1());
                break;
            case JOIN:
                Action a;
                while ((a = eventsToModele.remove()).getType() != ActionType.INIT_K3) {
                    modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setK3((Mountain) a.getData());
                while ((a = eventsToModele.remove()).getType() != ActionType.PLAYER_DATA) {
                    modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP1((Player) a.getData());
                while ((a = eventsToModele.remove()).getType() != ActionType.PLAYER_DATA) {
                    modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP2((Player) a.getData());
                k3.setCurrentPlayer(k3.getP2());
                break;
            case LOCAL:
                k3.setCurrentPlayer(k3.getP1());
                break;
        }
    }

    public void constructionPhase() {
        switch (getGameType()) {
            case HOST:
                constructionPhasePlayer(k3.getP1());
                waitConstruction(k3.getP2());
                break;
            case JOIN:
                constructionPhasePlayer(k3.getP2());
                waitConstruction(k3.getP1());
                break;
            case LOCAL:
                constructionPhasePlayer(k3.getP1());
                constructionPhasePlayer(k3.getP2());
                break;
        }
    }

    public void setFirstPlayer() {
        switch (getGameType()) {
            case HOST:
                k3.setCurrentPlayer(k3.getRandomPlayer());
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getCurrentPlayer()));
                if (k3.getCurrentPlayer().getId() != HOST) {
                    modeleToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                } else {
                    modeleToView.add(new Action(ActionType.ITS_YOUR_TURN));
                }
                waitAcknowledge();
                break;
            case JOIN:
                Action a;
                while ((a = eventsToModele.remove()).getType() != ActionType.PLAYER_DATA) {
                    modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                Player starter = (Player) a.getData();
                k3.setCurrentPlayer(k3.getPlayerById(starter.getId()));
                if (k3.getCurrentPlayer().getId() != JOIN) {
                    modeleToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                } else {
                    modeleToView.add(new Action(ActionType.ITS_YOUR_TURN));
                }
                acknowledge(true);
                break;
            case LOCAL:
                k3.setCurrentPlayer(k3.getRandomPlayer());
                break;
        }
    }

    public void gamePhase() {
        while (k3.canCurrentPlayerPlay()) {
            if (k3.getCurrentPlayer().isAI()) {
                Move move = k3.getCurrentPlayer().getAI().nextMove();
                playMove(new Action(ActionType.MOVE, move, k3.getCurrentPlayer().getId()));
            } else {
                Action a = controllerToModele.remove();
                switch (a.getType()) {
                    case MOVE:
                    case MOVE_NUMBER:
                        playMove(a);
                        break;
                    case UNDO:
                        undo();
                        break;
                    case REDO:
                        redo();
                        break;
                    default:
                        modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                        break;
                }
            }
        }
    }

    public void localGame(int type) {
        initPhase();
        constructionPhase();
        setFirstPlayer();
        modeleToView.add(new Action(ActionType.PRINT_STATE));
        gamePhase();

        if (k3.getCurrentPlayer() == k3.getP1()) {
            modeleToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP2()));
        } else {
            modeleToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP1()));
        }
    }

    synchronized public void swap(Swap s) {
        ModelColor c = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos1().x, s.getPos1().y);
        ModelColor c2 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos2().x, s.getPos2().y);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos1(), c2);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos2(), c);
    }

    public void playMove(Action a) {
        Move move;
        if (a.getType() == ActionType.MOVE_NUMBER) {
            try {
                move = k3.moveSet().get((int) a.getData());
            } catch (Exception e) {
                modeleToView.add(new Action(ActionType.MOVE, null));
                return;
            }
        } else {
            move = (Move) a.getData();
        }

        switch (getGameType()) {
            case LOCAL:
                if (k3.playMove(move)) {
                    modeleToView.add(new Action(ActionType.MOVE, move));
                } else {
                    modeleToView.add(new Action(ActionType.MOVE, move));
                }
                break;
            case HOST:
            case JOIN:
                if (a.getPlayer() != k3.getCurrentPlayer().getId()) {
                    modeleToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                } else if (a.getPlayer() != getGameType()) {
                    // Move from the outside
                    acknowledge(k3.playMove(move));
                    modeleToView.add(new Action(ActionType.ITS_YOUR_TURN));
                    modeleToView.add(new Action(ActionType.MOVE, move));
                } else {
                    // Local move
                    eventsToNetwork.add(new Action(ActionType.MOVE, move));
                    if (waitAcknowledge()) {
                        k3.playMove(move);
                        modeleToView.add(new Action(ActionType.MOVE, move));
                        if (k3.getCurrentPlayer().getId() != getGameType()){
                            modeleToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                        }
                    } else {

                    }
                }
                break;
        }

    }

    public void undo() {
        if (k3.getHistory().canUndo() && k3.unPlay()) {
            while (k3.getCurrentPlayer().isAI() && k3.getHistory().canUndo()) {
                k3.unPlay();
            }
            modeleToView.add(new Action(ActionType.UNDO, k3.getLastMovePlayed()));
        } else {
            modeleToView.add(new Action(ActionType.UNDO, null));
        }
    }

    public void redo() {
        if (k3.getHistory().canRedo() && k3.rePlay()) {
            modeleToView.add(new Action(ActionType.REDO, k3.getLastMovePlayed()));
        } else {
            modeleToView.add(new Action(ActionType.REDO, null));
        }
    }

    public void constructionPhaseIA(Player p) {
        p.getAI().constructionPhase();
        if (!p.getHasValidateBuilding()) {
            p.validateBuilding();
        }
        if (getGameType() != LOCAL) {
            eventsToNetwork.add(new Action(ActionType.VALIDATE, k3.getCurrentPlayer().clone()));
        }
        modeleToView.add(new Action(ActionType.VALIDATE, true));
        k3.updatePhase();
    }

    public void waitConstruction(Player p) {
        while (k3.getPhase() == Kube.PREPARATION_PHASE) {
            modeleToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            Action a = eventsToModele.remove();
            if (a.getType() == ActionType.VALIDATE && a.getPlayer() == p.getId()) {
                if (getGameType() == HOST) {
                    k3.setP2((Player) a.getData());
                } else {
                    k3.setP1((Player) a.getData());
                }
                k3.updatePhase();
            } else {
                modeleToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            }
        }
    }

    public void constructionPhasePlayer(Player p) {
        if (p.isAI()) {
            constructionPhaseIA(p);
        }
        while (!p.getHasValidateBuilding()) {
            Action a = eventsToModele.remove();
            switch (a.getType()) {
                case SWAP:
                    swap((Swap) a.getData());
                    modeleToView.add(a);
                    break;
                case SHUFFLE:
                    utilsAI.randomFillMountain(k3.getCurrentPlayer(), new Random());
                    modeleToView.add(new Action(ActionType.SHUFFLE));
                    break;
                case VALIDATE:
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
                            eventsToNetwork.add(new Action(ActionType.VALIDATE, k3.getCurrentPlayer().clone()));
                        }
                        k3.updatePhase();
                        modeleToView.add(new Action(ActionType.VALIDATE, isValidated));
                    }
                    break;
                default:
                    modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                    break;
            }
        }
    }

    public void acknowledge(boolean ack) {
        eventsToNetwork.add(new Action(ActionType.ACKNOWLEDGEMENT, ack));
    }

    public boolean waitAcknowledge() {
        modeleToView.add(new Action(ActionType.PRINT_WAITING_RESPONSE));
        Action a;
        while ((a = eventsToModele.remove()).getType() != ActionType.ACKNOWLEDGEMENT) {
            modeleToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
        }
        return (boolean) a.getData();
    }
}
