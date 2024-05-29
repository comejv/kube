package kube.model;

// Import model classes
import kube.configuration.Config;
import kube.model.action.*;
import kube.model.action.move.Move;
import kube.model.ai.utilsAI;

// Import java class
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game implements Runnable {

    /**********
     * CONSTANTS
     **********/

    public static final int LOCAL = 0;
    public static final int HOST = 1;
    public static final int JOIN = 2;

    public static final int RESET_EXIT = 0;
    public static final int NORMAL_EXIT = 1;

    public static final int CLASSIC_START = 0;
    public static final int LOAD_START = 1;

    public static final int PORT = 1234;

    /**********
     * ATTRIBUTES
     **********/

    Queue<Action> eventsToModel;
    Queue<Action> modelToView;
    Queue<Action> eventsToNetwork;
    private int gameType;
    private final Kube k3;

    /**********
     * CONSTRUCTOR
     **********/

    public Game(int gameType, Kube k3, Queue<Action> eventsToModel, Queue<Action> modelToView,
            Queue<Action> eventsToNetwork) {

        this.gameType = gameType;
        this.k3 = k3;
        this.eventsToModel = eventsToModel;
        this.eventsToNetwork = eventsToNetwork;
        this.modelToView = modelToView;
    }

    /**********
     * SETTERS
     **********/

    public final void setGameType(int gameType) {
        this.gameType = gameType;
    }

    /**********
     * GETTERS
     **********/

    public int getGameType() {
        return gameType;
    }

    /**********
     * RUN METHOD
     **********/

    @Override
    public void run() {
        while (true) {
            int startType = waitStartGame();
            localGame(gameType, startType);
        }
    }

    public int waitStartGame() {

        Action a;

        a = eventsToModel.remove();
        while (a.getType() != ActionType.START && a.getType() != ActionType.LOAD) {
            modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
            a = eventsToModel.remove();
        }

        if (a.getType() == ActionType.LOAD) {
            // Load the game
            File file = new File(Config.SAVING_PATH_DIRECTORY + a.getData().toString() + Config.SAVING_FILE_EXTENSION);
            try (FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                Config.debug(k3.getP1().getHasValidateBuilding());
                k3.init((Kube) ois.readObject());
                Config.debug(k3.getPhase());
                Config.debug(k3.getP1().getHasValidateBuilding());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return LOAD_START;
        } else {
            Start s = (Start) a.getData();
            if (s == null) {
                k3.init();
            } else {
                k3.init(s.getAiJ1(), s.getAiJ2());
            }
            return CLASSIC_START;
        }
    }

    public void localGame(int type, int startType) {
        if (startType == CLASSIC_START) {
            initPhase();
        }
        if (startType == CLASSIC_START || 
            (startType == LOAD_START && k3.getPhase() == Kube.PREPARATION_PHASE)) {
            if (constructionPhase() == RESET_EXIT) {
                return;
            }
            setFirstPlayer();
            modelToView.add(new Action(ActionType.PRINT_STATE));
        }
        if (gamePhase() == RESET_EXIT) {
            return;
        }
        modelToView.add(new Action(ActionType.ITS_YOUR_TURN));
        if (k3.getCurrentPlayer() == k3.getP1()) {
            modelToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP2()));
        } else {
            modelToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP1()));
        }
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
                while ((a = eventsToModel.remove()).getType() != ActionType.INIT_K3) {
                    modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setK3((Mountain) a.getData());
                while ((a = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP1((Player) a.getData());
                while ((a = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP2((Player) a.getData());
                k3.setCurrentPlayer(k3.getP2());
                break;
            case LOCAL:
                k3.setCurrentPlayer(k3.getP1());
                break;
        }
    }

    public int constructionPhase() {
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
                if (constructionPhasePlayer(k3.getP1()) == RESET_EXIT) {
                    return RESET_EXIT;
                }
                ;
                if (constructionPhasePlayer(k3.getP2()) == RESET_EXIT) {
                    return RESET_EXIT;
                }
                ;
                break;
        }
        return NORMAL_EXIT;
    }

    public int constructionPhasePlayer(Player p) {
        if (p.isAI()) {
            constructionPhaseIA(p);
            return NORMAL_EXIT;
        }
        while (!p.getHasValidateBuilding()) {

            Action a = eventsToModel.remove();
            Config.debug(a);
            switch (a.getType()) {
                case REMOVE:
                    remove((Remove) a.getData());
                    modelToView.add(a);
                    break;
                case BUILD:
                    build((Build) a.getData());
                    modelToView.add(a);
                    break;
                case SWAP:
                    swap((Swap) a.getData());
                    modelToView.add(a);
                    break;
                case SHUFFLE:
                    utilsAI.randomFillMountain(k3.getCurrentPlayer(), new Random());
                    modelToView.add(new Action(ActionType.SHUFFLE));
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
                        modelToView.add(new Action(ActionType.VALIDATE, isValidated));
                    }
                    break;
                case SAVE:
                    File file = new File(Config.SAVING_PATH_DIRECTORY + a.getData().toString() + Config.SAVING_FILE_EXTENSION);
                    try (FileOutputStream fos = new FileOutputStream(file);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                        oos.writeObject(k3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case RESET:
                    return RESET_EXIT;
                default:
                    modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                    break;
            }
        }
        return NORMAL_EXIT;
    }

    public void constructionPhaseIA(Player p) {
        p.getAI().constructionPhase(k3);
        if (!p.getHasValidateBuilding()) {
            p.validateBuilding();
        }
        if (getGameType() != LOCAL) {
            eventsToNetwork.add(new Action(ActionType.VALIDATE, k3.getCurrentPlayer().clone()));
        }
        modelToView.add(new Action(ActionType.VALIDATE, true));
        k3.updatePhase();
    }

    public void setFirstPlayer() {
        switch (getGameType()) {
            case HOST:
                k3.setCurrentPlayer(k3.getRandomPlayer());
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getCurrentPlayer()));
                if (k3.getCurrentPlayer().getId() == HOST) {
                    modelToView.add(new Action(ActionType.ITS_YOUR_TURN));
                } else {
                    modelToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                }
                waitAcknowledge();
                break;
            case JOIN:
                Action a;
                while ((a = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                Player starter = (Player) a.getData();
                k3.setCurrentPlayer(k3.getPlayerById(starter.getId()));
                if (k3.getCurrentPlayer().getId() == JOIN) {
                    modelToView.add(new Action(ActionType.ITS_YOUR_TURN));
                } else {
                    modelToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                }
                acknowledge(true);
                break;
            case LOCAL:
                k3.setCurrentPlayer(k3.getRandomPlayer());
                break;
        }
    }

    public int gamePhase() {
        while (k3.canCurrentPlayerPlay()) {
            if (k3.getCurrentPlayer().isAI()) {
                Move move = k3.getCurrentPlayer().getAI().nextMove(k3);
                playMove(new Action(ActionType.MOVE, move, k3.getCurrentPlayer().getId()));
            } else {
                Action a = eventsToModel.remove();
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
                    case SAVE:
                        File file = new File(Config.SAVING_PATH_DIRECTORY + a.getData().toString() + Config.SAVING_FILE_EXTENSION);
                        try (FileOutputStream fos = new FileOutputStream(file);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                            oos.writeObject(k3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case RESET:
                        return RESET_EXIT;
                    default:
                        modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                        break;
                }
            }
        }
        return NORMAL_EXIT;
    }

    synchronized public void swap(Swap s) {

        ModelColor c = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos1().x, s.getPos1().y);
        ModelColor c2 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getPos2().x, s.getPos2().y);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos1(), c2);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getPos2(), c);
    }

    synchronized public boolean build(Build b) {
        return k3.getCurrentPlayer().addToMountainFromAvailableToBuild(b.getPos(), b.getModelColor());
    }

    synchronized public ModelColor remove(Remove r) {
        return k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(r.getPos());
    }

    public void playMove(Action a) {
        Move move;
        if (a.getType() == ActionType.MOVE_NUMBER) {
            try {
                move = k3.moveSet().get((int) a.getData());
            } catch (Exception e) {
                modelToView.add(new Action(ActionType.MOVE, null));
                return;
            }
        } else {
            move = (Move) a.getData();
        }
        switch (getGameType()) {
            case LOCAL:
                if (k3.playMove(move)) {
                    modelToView.add(new Action(ActionType.MOVE, move));
                } else {
                    modelToView.add(new Action(ActionType.MOVE, move));
                }
                break;
            case HOST:
            case JOIN:
                if (a.getPlayer() == getGameType() && a.getPlayer() != k3.getCurrentPlayer().getId()) {
                    modelToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                } else if (a.getPlayer() != getGameType()) {
                    // Move from the outside
                    boolean validMove = k3.playMove(move);
                    acknowledge(validMove);
                    if (validMove) {
                        modelToView.add(new Action(ActionType.ITS_YOUR_TURN));
                        modelToView.add(new Action(ActionType.MOVE, move));
                    }
                } else {
                    // Local move
                    eventsToNetwork.add(new Action(ActionType.MOVE, move));
                    if (waitAcknowledge() && k3.playMove(move)) {
                        modelToView.add(new Action(ActionType.MOVE, move));
                        if (k3.getCurrentPlayer().getId() != getGameType()) {
                            modelToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
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
            modelToView.add(new Action(ActionType.UNDO, k3.getLastMovePlayed()));
        } else {
            modelToView.add(new Action(ActionType.UNDO, null));
        }
    }

    public void redo() {
        if (k3.getHistory().canRedo() && k3.rePlay()) {
            modelToView.add(new Action(ActionType.REDO, k3.getLastMovePlayed()));
        } else {
            modelToView.add(new Action(ActionType.REDO, null));
        }
    }

    public void waitConstruction(Player p) {
        while (k3.getPhase() == Kube.PREPARATION_PHASE) {
            modelToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            Action a = eventsToModel.remove();
            if (a.getType() == ActionType.VALIDATE && a.getPlayer() == p.getId()) {
                if (getGameType() == HOST) {
                    k3.setP2((Player) a.getData());
                } else {
                    k3.setP1((Player) a.getData());
                }
                k3.updatePhase();
            } else {
                modelToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            }
        }
    }

    public void acknowledge(boolean ack) {
        eventsToNetwork.add(new Action(ActionType.ACKNOWLEDGEMENT, ack));
    }

    public boolean waitAcknowledge() {
        modelToView.add(new Action(ActionType.PRINT_WAITING_RESPONSE));
        Action a;
        while ((a = eventsToModel.remove()).getType() != ActionType.ACKNOWLEDGEMENT) {
            modelToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
        }
        return (boolean) a.getData();
    }
}
