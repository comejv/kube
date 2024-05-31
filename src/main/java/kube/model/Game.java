package kube.model;

// Import model classes
import kube.configuration.Config;
import kube.model.action.*;
import kube.model.action.move.Move;
import kube.model.ai.moveSetHeuristique;
import kube.model.ai.utilsAI;

import java.util.Calendar;
// Import java class
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;

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
    public static final int LOAD_ERROR = 2;

    public static final int PORT = 1234;

    /**********
     * ATTRIBUTES
     **********/

    Queue<Action> eventsToModel;
    Queue<Action> eventsToView;
    Queue<Action> eventsToNetwork;
    private int gameType;
    private final Kube k3;

    /**********
     * CONSTRUCTOR
     **********/

    public Game(int gameType, Kube k3, Queue<Action> eventsToModel, Queue<Action> eventsToView,
            Queue<Action> eventsToNetwork) {

        this.gameType = gameType;
        this.k3 = k3;
        this.eventsToModel = eventsToModel;
        this.eventsToNetwork = eventsToNetwork;
        this.eventsToView = eventsToView;
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
            if (startType == LOAD_ERROR) {
                continue;
            }
            localGame(gameType, startType);
        }
    }

    public int waitStartGame() {
        Action a;
        a = eventsToModel.remove();
        while (a.getType() != ActionType.START && a.getType() != ActionType.LOAD) {
            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
            a = eventsToModel.remove();
        }

        if (a.getType() == ActionType.LOAD) {
            // Load the game
            String filePath;
            filePath = Config.SAVING_PATH_DIRECTORY + (String) a.getData() + Config.SAVING_FILE_EXTENSION;
            File file = new File(filePath);
            try (FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                k3.init((Kube) ois.readObject());
            } catch (Exception e) {
                eventsToView.add(new Action(ActionType.PRINT_WRONG_FILE_NAME));
                return LOAD_ERROR;
            }
            return LOAD_START;
        } else {
            Start s = (Start) a.getData();
            if (s == null) {
                k3.init();
            } else {
                k3.init(s.getAIJ1(), s.getAIJ2());
            }
            eventsToView.add(new Action(ActionType.VALIDATE, true));
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
            eventsToView.add(new Action(ActionType.PRINT_STATE));
        }
        if (gamePhase() == RESET_EXIT) {
            return;
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
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setK3((Mountain) a.getData());
                while ((a = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP1((Player) a.getData());
                while ((a = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
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
            p.getAI().constructionPhase(k3);
        }
        while (!p.getIsMountainValidated()) {
            Action a = eventsToModel.remove();
            switch (a.getType()) {
                case AI_MOVE:
                    constructionPhaseAIsuggestion(p);
                    eventsToView.add(a);
                    break;
                case REMOVE:
                    remove((Remove) a.getData());
                    eventsToView.add(a);
                    break;
                case BUILD:
                    build((Build) a.getData());
                    eventsToView.add(a);
                    break;
                case SWAP:
                    swap((Swap) a.getData());
                    eventsToView.add(a);
                    break;
                case SHUFFLE:
                    utilsAI.randomFillMountain(k3.getCurrentPlayer(), new Random());
                    eventsToView.add(new Action(ActionType.SHUFFLE));
                    break;
                case VALIDATE:
                    // Reception of the other player mountain
                    if (getGameType() != LOCAL && a.getPlayerId() != getGameType()) {
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
                        eventsToView.add(new Action(ActionType.VALIDATE, isValidated));
                    }
                    break;
                case SAVE:
                    save(a.getData().toString());
                    break;
                case RESET:
                    return RESET_EXIT;
                default:
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                    break;
            }
        }
        return NORMAL_EXIT;
    }

    public void setFirstPlayer() {
        switch (getGameType()) {
            case HOST:
                k3.setCurrentPlayer(k3.getRandomPlayer());
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getCurrentPlayer()));
                if (k3.getCurrentPlayer().getId() == HOST) {
                    eventsToView.add(new Action(ActionType.ITS_YOUR_TURN));
                } else {
                    eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                }
                waitAcknowledge();
                break;
            case JOIN:
                Action a;
                while ((a = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                Player starter = (Player) a.getData();
                k3.setCurrentPlayer(k3.getPlayerById(starter.getId()));
                if (k3.getCurrentPlayer().getId() == JOIN) {
                    eventsToView.add(new Action(ActionType.ITS_YOUR_TURN));
                } else {
                    eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                }
                acknowledge(true);
                break;
            case LOCAL:
                k3.setCurrentPlayer(k3.getRandomPlayer());
                break;
        }
    }

    public int gamePhase() {

        Action a;
        while (true) {

            while (!k3.canCurrentPlayerPlay()) {
                a = eventsToModel.remove();
                switch (a.getType()) {
                    case UNDO:
                        undo();
                        break;
                    case SAVE:
                        Config.debug("JE SUIS LAAAAAAAAAAAAAAAA 1");
                        save(a.getData().toString());
                        break;
                    case RESET:
                        return RESET_EXIT;
                    default:
                        break;
                }
            }

            while (k3.canCurrentPlayerPlay()) {
                if (k3.getCurrentPlayer().isAI()) {
                    Move move = k3.getCurrentPlayer().getAI().nextMove(k3);
                    playMove(new Action(ActionType.MOVE, move, k3.getCurrentPlayer().getId()));
                } else {
                    a = eventsToModel.remove();
                    switch (a.getType()) {
                        case MOVE:
                        case MOVE_NUMBER:
                        case CREATE_MOVE:
                            playMove(a);
                            break;
                        case UNDO:
                            undo();
                            break;
                        case REDO:
                            redo();
                            break;
                        case SAVE:
                            Config.debug("JE SUIS LAAAAAAAAAAAAAAAA 2");
                            save(a.getData().toString());
                            break;
                        case RESET:
                            return RESET_EXIT;
                        default:
                            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION, a.getData()));
                            break;
                    }
                }
            }

            eventsToView.add(new Action(ActionType.ITS_YOUR_TURN));
            if (k3.getCurrentPlayer() == k3.getP1()) {
                eventsToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP2()));
            } else {
                eventsToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP1()));
            }
        }
    }

    synchronized public void swap(Swap s) {

        ModelColor c = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getFrom().x, s.getFrom().y);
        ModelColor c2 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(s.getTo().x, s.getTo().y);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getFrom(), c2);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(s.getTo(), c);
    }

    synchronized public boolean build(Build b) {
        return k3.getCurrentPlayer().addToMountainFromAvailableToBuild(b.getPosition(), b.getModelColor());
    }

    synchronized public ModelColor remove(Remove r) {
        return k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(r.getPosition());
    }

    public void playMove(Action a) {
        Move move;
        switch (a.getType()) {
            case MOVE_NUMBER:
                try {
                    move = k3.moveSet().get((int) a.getData());
                } catch (Exception e) {
                    eventsToView.add(new Action(ActionType.MOVE, null));
                    return;
                }
                break;
            case CREATE_MOVE:
                CreateMove cM = (CreateMove) a.getData();
                move = k3.createMove(cM.getFrom(), cM.getPlayerFrom(), cM.getTo(), cM.getPlayerTo(), cM.getModelColor());
                Config.debug("Move généré :", move);
                break;
            case MOVE:
            default:
                move = (Move) a.getData();
                break;
        }
        if (move == null){
            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
            return;
        }
        switch (getGameType()) {
            case LOCAL:
                if (k3.playMove(move)) {
                    eventsToView.add(new Action(ActionType.MOVE, move));
                } else {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                break;
            case HOST:
            case JOIN:
                if (a.getPlayerId() == getGameType() && a.getPlayerId() != k3.getCurrentPlayer().getId()) {
                    eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                } else if (a.getPlayerId() != getGameType()) {
                    // Move from the outside
                    boolean validMove = k3.playMove(move);
                    acknowledge(validMove);
                    if (validMove) {
                        eventsToView.add(new Action(ActionType.ITS_YOUR_TURN));
                        eventsToView.add(new Action(ActionType.MOVE, move));
                    } else {
                        eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                    }
                } else {
                    // Local move
                    eventsToNetwork.add(new Action(ActionType.MOVE, move));
                    if (waitAcknowledge() && k3.playMove(move)) {
                        eventsToView.add(new Action(ActionType.MOVE, move));
                        if (k3.getCurrentPlayer().getId() != getGameType()) {
                            eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                        }
                    } else {
                        eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
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
            eventsToView.add(new Action(ActionType.UNDO, k3.getLastMovePlayed()));
        } else {
            eventsToView.add(new Action(ActionType.UNDO, null));
        }
    }

    public void redo() {
        if (k3.getHistory().canRedo() && k3.rePlay()) {
            eventsToView.add(new Action(ActionType.REDO, k3.getLastMovePlayed()));
        } else {
            eventsToView.add(new Action(ActionType.REDO, null));
        }
    }

    public void constructionPhaseAIsuggestion(Player p) {
        for (int i = 0; i < p.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                p.removeFromMountainToAvailableToBuild(i, j);
            }
        }
        p.setAI(new moveSetHeuristique());
        p.getAI().setPlayerId(p.getId());
        p.getAI().constructionPhase(k3);
    }

    public void waitConstruction(Player p) {
        while (k3.getPhase() == Kube.PREPARATION_PHASE) {
            eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            Action a = eventsToModel.remove();
            if (a.getType() == ActionType.VALIDATE && a.getPlayerId() == p.getId()) {
                if (getGameType() == HOST) {
                    k3.setP2((Player) a.getData());
                } else {
                    k3.setP1((Player) a.getData());
                }
                k3.updatePhase();
            } else {
                eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            }
        }
    }

    public void acknowledge(boolean ack) {
        eventsToNetwork.add(new Action(ActionType.ACKNOWLEDGEMENT, ack));
    }

    public boolean waitAcknowledge() {
        eventsToView.add(new Action(ActionType.PRINT_WAITING_RESPONSE));
        Action a;
        while ((a = eventsToModel.remove()).getType() != ActionType.ACKNOWLEDGEMENT) {
            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
        }
        return (boolean) a.getData();
    }

    private void save(String fileName) {
        String filePath;
        if (fileName == "") {
            String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().getTime());
            fileName = timeStamp;
        }
        File directory = new File(Config.SAVING_PATH_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        filePath = Config.SAVING_PATH_DIRECTORY + fileName + Config.SAVING_FILE_EXTENSION;
        File file = new File(Config.SAVING_PATH_DIRECTORY + fileName + Config.SAVING_FILE_EXTENSION);
        try (FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(k3);
            eventsToView.add(new Action(ActionType.PRINT_SAVED, filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
