package kube.model;

// Import model classes
import kube.configuration.Config;
import kube.model.action.*;
import kube.model.action.move.Move;
import kube.model.ai.betterConstructV2;
import kube.model.ai.moveSetHeuristique;
import kube.model.ai.utilsAI;

// Import java class
import java.util.Calendar;
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

    public static final int CLASSIC_START = 1;
    public static final int LOAD_START = 2;
    public static final int LOAD_ERROR = 3;
    public static final int ERROR_START = 4;

    public static final int PORT = 1234;

    /**********
     * ATTRIBUTES
     **********/

    Queue<Action> eventsToModel, eventsToView, eventsToNetwork;
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
            int startType;
            startType = waitStartGame();
            if (startType == LOAD_ERROR || startType == RESET_EXIT) {
                continue;
            }
            localGame(startType);
        }
    }

    /**
     * Wait for the start of the game (START or LOAD action)
     * 
     * @return the type of start (CLASSIC_START, LOAD_START or LOAD_ERROR)
     */
    public int waitStartGame() {

        Action action;
        String filePath;
        File file;
        Start start;
        FileInputStream fis;
        ObjectInputStream ois;

        action = eventsToModel.remove();

        while (action.getType() != ActionType.START && action.getType() != ActionType.LOAD &&
                action.getType() != ActionType.RESET) {
            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
            action = eventsToModel.remove();
        }

        switch (action.getType()) {
            case START:
                // Start a new game
                start = (Start) action.getData();
                if (start == null) {
                    k3.init();
                } else {
                    k3.init(start.getAIJ1(), start.getAIJ2());
                }
                eventsToView.add(new Action(ActionType.VALIDATE, true));
                return CLASSIC_START;
            case LOAD:
                // Load a saved game
                filePath = Config.SAVING_PATH_DIRECTORY + (String) action.getData();
                file = new File(filePath);
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    k3.init((Kube) ois.readObject());
                    ois.close();
                    Config.debug("initilized game");
                    eventsToView.add(new Action(ActionType.VALIDATE, true));
                    return LOAD_START;
                } catch (Exception e) {
                    Config.error(e);
                    eventsToView.add(new Action(ActionType.PRINT_WRONG_FILE_NAME));
                    return LOAD_ERROR;
                }
            case RESET:
                Config.debug("Received RESET action");
                return RESET_EXIT;
            default:
                eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                return ERROR_START;
        }
    }

    /**
     * Start a local game
     * 
     * @param startType the type of start (CLASSIC_START or LOAD_START)
     * @return void
     */
    public void localGame(int startType) {
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

    /**
     * Initialize the game
     * 
     * @return void
     */
    public void initPhase() {
        Action action;
        switch (getGameType()) {
            case HOST:
                eventsToNetwork.add(new Action(ActionType.INIT_K3, k3.getK3().clone()));
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getP1().clone()));
                eventsToNetwork.add(new Action(ActionType.PLAYER_DATA, k3.getP2().clone()));
                k3.setCurrentPlayer(k3.getP1());
                break;
            case JOIN:
                while ((action = eventsToModel.remove()).getType() != ActionType.INIT_K3) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setK3((Mountain) action.getData());
                while ((action = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP1((Player) action.getData());
                while ((action = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                k3.setP2((Player) action.getData());
                k3.setCurrentPlayer(k3.getP2());
                break;
            case LOCAL:
                k3.setCurrentPlayer(k3.getP1());
                break;
        }
    }

    /**
     * Process the construction phase
     * 
     * @return the exit code (RESET_EXIT or NORMAL_EXIT)
     */
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
                if (constructionPhasePlayer(k3.getP2()) == RESET_EXIT) {
                    return RESET_EXIT;
                }
                break;
        }
        return NORMAL_EXIT;
    }

    /**
     * Process the construction phase for a player
     * 
     * @param player the player
     * @return the exit code (RESET_EXIT or NORMAL_EXIT)
     */
    public int constructionPhasePlayer(Player player) {

        Action action;
        boolean isValidated;
        action = eventsToModel.peak();

        if (player.isAI()) {
            player.getAI().constructionPhase(k3);
        }

        while (!player.getIsMountainValidated()) {

            action = eventsToModel.remove();

            switch (action.getType()) {
                case AI_MOVE:
                    constructionPhaseAIsuggestion(player);
                    eventsToView.add(action);
                    break;
                case REMOVE:
                    remove((Remove) action.getData());
                    eventsToView.add(action);
                    break;
                case BUILD:
                    build((Build) action.getData());
                    eventsToView.add(action);
                    break;
                case SWAP:
                    swap((Swap) action.getData());
                    eventsToView.add(action);
                    break;
                case SHUFFLE:
                    utilsAI.randomFillMountain(k3.getCurrentPlayer(), new Random());
                    eventsToView.add(new Action(ActionType.SHUFFLE));
                    break;
                case VALIDATE:
                    // Reception of the other player mountain
                    if (getGameType() != LOCAL && action.getPlayerId() != getGameType()) {
                        if (getGameType() == JOIN) {
                            k3.setP1((Player) action.getData());
                        } else {
                            k3.setP2((Player) action.getData());
                        }
                    } else {
                        isValidated = k3.getCurrentPlayer().validateBuilding();
                        if (isValidated && getGameType() != LOCAL) {
                            eventsToNetwork.add(new Action(ActionType.VALIDATE, k3.getCurrentPlayer().clone()));
                        }
                        k3.updatePhase();
                        eventsToView.add(new Action(ActionType.VALIDATE, isValidated));
                    }
                    break;
                case SAVE:
                    Config.debug("Save");
                    save(action.getData().toString());
                    break;
                case RESET:
                    return RESET_EXIT;
                case AI_PAUSE:
                    break;
                default:
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                    break;
            }
        }
        return NORMAL_EXIT;
    }

    /**
     * Set the first player
     * 
     * @return void
     */
    public void setFirstPlayer() {

        Action action;
        Player firstPlayer;

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
                while ((action = eventsToModel.remove()).getType() != ActionType.PLAYER_DATA) {
                    eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
                }
                firstPlayer = (Player) action.getData();
                k3.setCurrentPlayer(k3.getPlayerById(firstPlayer.getId()));
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

    /**
     * Process the game phase
     * 
     * @return the exit code (RESET_EXIT or NORMAL_EXIT)
     */
    public int gamePhase() {
        Boolean AIpause = false;
        Action action;
        // Instant loose case
        if (!k3.canCurrentPlayerPlay()) {
            eventsToView.add(new Action(ActionType.ITS_YOUR_TURN));
            if (k3.getCurrentPlayer() == k3.getP1()) {
                eventsToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP2()));
            } else {
                eventsToView.add(new Action(ActionType.PRINT_WIN_MESSAGE, k3.getP1()));
            }
        }

        while (true) {

            while (!k3.canCurrentPlayerPlay()) {
                action = eventsToModel.remove();
                switch (action.getType()) {
                    case UNDO:
                        AIpause = true;
                        eventsToView.add(new Action(ActionType.AI_PAUSE, true));
                        undo();
                        break;
                    case SAVE:
                        save(action.getData().toString());
                        break;
                    case RESET:
                        return RESET_EXIT;
                    case AI_PAUSE:
                        AIpause = (Boolean) action.getData();
                        break;
                    default:
                        break;
                }
            }

            while (k3.canCurrentPlayerPlay()) {
                if (!eventsToModel.isEmpty() && eventsToModel.peak().getType() == ActionType.AI_PAUSE) {
                    action = eventsToModel.remove();
                    AIpause = (Boolean) action.getData();
                }
                if (k3.getCurrentPlayer().isAI() && !AIpause) {
                    Move move = k3.getCurrentPlayer().getAI().nextMove(k3);
                    if (!eventsToModel.isEmpty() && eventsToModel.peak().getType() == ActionType.AI_PAUSE) {
                        action = eventsToModel.remove();
                        AIpause = (Boolean) action.getData();
                    }
                    if (!AIpause) { // Verify if a pause as be sent during the AI move search
                        playMove(new Action(ActionType.MOVE, move, k3.getCurrentPlayer().getId()));
                    }
                } else {
                    action = eventsToModel.remove();
                    switch (action.getType()) {
                        case MOVE:
                        case MOVE_NUMBER:
                        case CREATE_MOVE:
                            if (k3.getCurrentPlayer().isAI()) {
                                break;
                            }
                            playMove(action);
                            break;
                        case UNDO:
                            AIpause = true;
                            eventsToView.add(new Action(ActionType.AI_PAUSE, true));
                            undo();
                            break;
                        case REDO:
                            AIpause = true;
                            eventsToView.add(new Action(ActionType.AI_PAUSE, true));
                            redo();
                            break;
                        case SAVE:
                            save(action.getData().toString());
                            break;
                        case RESET:
                            return RESET_EXIT;
                        case AI_PAUSE:
                            AIpause = (Boolean) action.getData();
                            break;
                        case AI_MOVE:
                            if (k3.getCurrentPlayer().isAI()) {
                                break;
                            }
                            k3.getCurrentPlayer().setAI(new betterConstructV2(50));
                            k3.getCurrentPlayer().getAI().setPlayerId(k3.getCurrentPlayer().getId());
                            Move move = k3.getCurrentPlayer().getAI().nextMove(k3);
                            playMove(new Action(ActionType.MOVE, move, k3.getCurrentPlayer().getId()));
                            break;
                        default:
                            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION, action.getData()));
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

    /**********
     * METHODS
     **********/

    /**
     * Swap two cubes
     * 
     * @param swap the swap action
     * @return void
     */
    synchronized public void swap(Swap swap) {

        ModelColor color1, color2;

        color1 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(swap.getFrom().x, swap.getFrom().y);
        color2 = k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(swap.getTo().x, swap.getTo().y);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(swap.getFrom(), color2);
        k3.getCurrentPlayer().addToMountainFromAvailableToBuild(swap.getTo(), color1);
    }

    /**
     * Build a cube
     * 
     * @param build the build action
     * @return true if the cube has been built, false otherwise
     */
    synchronized public boolean build(Build build) {
        return k3.getCurrentPlayer().addToMountainFromAvailableToBuild(build.getPosition(), build.getModelColor());
    }

    /**
     * Remove a cube
     * 
     * @param remove the remove action
     * @return the removed cube color
     */
    synchronized public ModelColor remove(Remove remove) {
        return k3.getCurrentPlayer().removeFromMountainToAvailableToBuild(remove.getPosition());
    }

    /**
     * Play a move
     * 
     * @param action the action to play
     * @return void
     */
    public void playMove(Action action) {

        Move move;
        CreateMove currentMove;
        boolean isValidMove;

        switch (action.getType()) {
            case MOVE_NUMBER:
                try {
                    move = k3.moveSet().get((int) action.getData());
                } catch (Exception e) {
                    eventsToView.add(new Action(ActionType.MOVE, null));
                    return;
                }
                break;
            case CREATE_MOVE:
                currentMove = (CreateMove) action.getData();
                move = k3.createMove(currentMove.getFrom(), currentMove.getPlayerFrom(), currentMove.getTo(),
                        currentMove.getPlayerTo(),
                        currentMove.getModelColor());
                break;
            case MOVE:
            default:
                move = (Move) action.getData();
                break;
        }

        if (move == null) {
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
                if (action.getPlayerId() == getGameType() && action.getPlayerId() != k3.getCurrentPlayer().getId()) {
                    eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
                } else if (action.getPlayerId() != getGameType()) {
                    // Move from the outside
                    isValidMove = k3.playMove(move);
                    acknowledge(isValidMove);
                    if (isValidMove) {
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

    /**
     * Undo the last move
     * 
     * @return void
     */
    public void undo() {
        if (k3.getHistory().canUndo() && k3.unPlay()) {
            eventsToView.add(new Action(ActionType.UNDO, k3.getLastMovePlayed()));
        } else {
            eventsToView.add(new Action(ActionType.UNDO, null));
        }
    }

    /**
     * Redo the last move
     * 
     * @return void
     */
    public void redo() {

        if (k3.getHistory().canRedo() && k3.rePlay()) {
            eventsToView.add(new Action(ActionType.REDO, k3.getLastMovePlayed()));
        } else {
            eventsToView.add(new Action(ActionType.REDO, null));
        }
    }

    /**
     * Process the AI suggestion for the construction phase
     * 
     * @param player the player
     * @return void
     */
    public void constructionPhaseAIsuggestion(Player player) {

        int i, j;

        for (i = 0; i < player.getMountain().getBaseSize(); i++) {
            for (j = 0; j < i + 1; j++) {
                player.removeFromMountainToAvailableToBuild(i, j);
            }
        }

        player.setAI(new betterConstructV2());
        player.getAI().setPlayerId(player.getId());
        player.getAI().constructionPhase(k3);
        player.setAI(null);
    }

    /**
     * Wait for the construction phase of a player
     * 
     * @param player the player
     * @return void
     */
    public void waitConstruction(Player player) {

        Action action;

        while (k3.getPhase() == Kube.PREPARATION_PHASE) {

            eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            action = eventsToModel.remove();

            if (action.getType() == ActionType.VALIDATE && action.getPlayerId() == player.getId()) {
                if (getGameType() == HOST) {
                    k3.setP2((Player) action.getData());
                } else {
                    k3.setP1((Player) action.getData());
                }
                k3.updatePhase();
            } else {
                eventsToView.add(new Action(ActionType.PRINT_NOT_YOUR_TURN));
            }
        }
    }

    /**
     * Acknowledge an action
     * 
     * @param ack the acknowledgement
     * @return void
     */
    public void acknowledge(boolean ack) {
        eventsToNetwork.add(new Action(ActionType.ACKNOWLEDGEMENT, ack));
    }

    /**
     * Wait for an acknowledgement
     * 
     * @return the acknowledgement
     */
    public boolean waitAcknowledge() {

        Action a;

        eventsToView.add(new Action(ActionType.PRINT_WAITING_RESPONSE));

        while ((a = eventsToModel.remove()).getType() != ActionType.ACKNOWLEDGEMENT) {
            eventsToView.add(new Action(ActionType.PRINT_FORBIDDEN_ACTION));
        }
        return (boolean) a.getData();
    }

    /**
     * Save the game
     * 
     * @param fileName the file name
     * @return void
     */
    private void save(String fileName) {

        String filePath, timeStamp;
        File directory, file;
        FileOutputStream fos;
        ObjectOutputStream oos;

        if (fileName == "") {
            timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().getTime());
            fileName = timeStamp;
        }

        directory = new File(Config.SAVING_PATH_DIRECTORY);
        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        filePath = Config.SAVING_PATH_DIRECTORY + fileName + Config.SAVING_FILE_EXTENSION;
        file = new File(Config.SAVING_PATH_DIRECTORY + fileName + Config.SAVING_FILE_EXTENSION);

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(k3);
            oos.close();
            eventsToView.add(new Action(ActionType.PRINT_SAVED, filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
