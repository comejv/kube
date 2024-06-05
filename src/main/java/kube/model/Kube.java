package kube.model;

import kube.model.action.move.Move;
import kube.model.action.move.MoveAA;
import kube.model.action.move.MoveAM;
import kube.model.action.move.MoveAW;
import kube.model.action.move.MoveMA;
import kube.model.action.move.MoveMM;
import kube.model.action.move.MoveMW;
import kube.model.ai.MiniMaxAI;

// Import java classes
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Point;

public class Kube implements Serializable {

    /**********
     * CONSTANTS
     **********/

    public static final int NB_CUBE_PER_COLOR = 9;
    public static final int PREPARATION_PHASE = 1;
    public static final int GAME_PHASE = 2;
    public static final int ID_PLAYER_1 = 1;
    public static final int ID_PLAYER_2 = 2;

    public static final String NOT_IN_PREPARATION_PHASE = "Forbidden operation, the Kube isn't in preparation phase";
    public static final String NOT_IN_GAME_PHASE = "Forbidden operation, the Kube isn't in game phase";

    /**********
     * ATTRIBUTES
     **********/

    private Player p1, p2, currentPlayer;
    private ArrayList<ModelColor> bag;
    private boolean penalty;
    private History history;
    private int baseSize, phase;
    private Mountain mountain;
    private Move lastMovePlayed;
    private transient int gameType;
    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the Kube
     */
    public Kube() {
        init();
    }

    /**
     * Constructor of the Kube (empty or not)
     * 
     * @param empty true if the Kube is empty, false otherwise
     */
    public Kube(boolean empty) {
        if (!empty) {
            init();
        }
    }

    /**********
     * INITIALIZATION
     **********/

    /**
     * Initialize the Kube with the default values
     * 
     * @return void
     */
    public final void init() {
        init(null, null, new Random());
    }

    /**
     * Initialize the Kube with the default values and the given type of AI
     * 
     * @param typeAI1 the type of AI for the player 1
     * @return void
     */
    public void init(MiniMaxAI typeAI1) {
        init(typeAI1, null, new Random());
    }

    /**
     * Initialize the Kube with the default values and the given type of AI
     * 
     * @param typeAI1 the type of AI for the player 1
     * @param typeAI2 the type of AI for the player 2
     * @return void
     */
    public void init(MiniMaxAI typeAI1, MiniMaxAI typeAI2) {
        init(typeAI1, typeAI2, new Random());
    }

    /**
     * Initialize the Kube with the default values and the given type of AI
     * 
     * @param typeAI1 the type of AI for the player 1
     * @param typeAI2 the type of AI for the player 2
     * @param seed    the seed to shuffle the bag
     * @return void
     */
    public void init(MiniMaxAI typeAI1, MiniMaxAI typeAI2, int seed) {
        init(typeAI1, typeAI2, new Random(seed));
    }

    /**
     * Initialize the Kube with the default values and the given type of AI
     * 
     * @param typeAI1 the type of AI for the player 1
     * @param typeAI2 the type of AI for the player 2
     * @param random  the random object to shuffle the bag
     * @return void
     */
    public void init(MiniMaxAI typeAI1, MiniMaxAI typeAI2, Random random) {

        setBaseSize(9);
        setPhase(PREPARATION_PHASE);
        setMountain(new Mountain(getBaseSize()));
        setBag(new ArrayList<>());
        fillBag(random);
        fillBase();
        setHistory(new History());
        setPenalty(false);

        if (typeAI1 != null) {
            setP1(new AI(ID_PLAYER_1, typeAI1));
        } else {
            setP1(new Player(ID_PLAYER_1));
        }

        if (typeAI2 != null) {
            setP2(new AI(ID_PLAYER_2, typeAI2));
        } else {
            setP2(new Player(ID_PLAYER_2));
        }

        setCurrentPlayer(getP1());
        distributeCubesToPlayers();
    }

    /**
     * Initialize the Kube with the given Kube
     * 
     * @param kube the Kube to copy
     * @return void
     */
    public void init(Kube kube) {

        if (kube.getP1() instanceof AI) {
            setP1(new AI(ID_PLAYER_1, ((AI) kube.getP1()).getAI()));
        } else {
            setP1(new Player(ID_PLAYER_1));
        }

        getP1().setName(new String(kube.getP1().getName()));
        getP1().setMountain(kube.getP1().getMountain().clone());
        getP1().setIsMountainValidated(kube.getP1().getIsMountainValidated());
        getP1().setAdditionals(new ArrayList<>(kube.getP1().getAdditionals()));
        getP1().setAvailableToBuild(new HashMap<>(kube.getP1().getAvailableToBuild()));
        getP1().setUsedPiece(new HashMap<>(kube.getP1().getUsedPiece()));

        if (kube.getP2() instanceof AI) {
            setP2(new AI(ID_PLAYER_2, ((AI) kube.getP2()).getAI()));
        } else {
            setP2(new Player(ID_PLAYER_2));
        }

        getP2().setName(new String(kube.getP2().getName()));
        getP2().setMountain(kube.getP2().getMountain().clone());
        getP2().setIsMountainValidated(kube.getP2().getIsMountainValidated());
        getP2().setAdditionals(new ArrayList<>(kube.getP2().getAdditionals()));
        getP2().setAvailableToBuild(new HashMap<>(kube.getP2().getAvailableToBuild()));
        getP2().setUsedPiece(new HashMap<>(kube.getP2().getUsedPiece()));

        if (kube.getCurrentPlayer() == kube.getP1()) {
            setCurrentPlayer(getP1());
        } else {
            setCurrentPlayer(getP2());
        }

        setBag(new ArrayList<>(kube.getBag()));
        setPenalty(kube.getPenalty());

        setHistory(new History());
        getHistory().setDone(new CopyOnWriteArrayList<>(kube.getHistory().getDone()));
        getHistory().setUndone(new CopyOnWriteArrayList<>(kube.getHistory().getUndone()));

        setBaseSize(kube.getBaseSize());
        setMountain(kube.getMountain().clone());
        setPhase(kube.getPhase());
        setLastMovePlayed(kube.getLastMovePlayed());
    }

    /**********
     * SETTERS
     **********/

    public void setBag(ArrayList<ModelColor> b) {
        bag = b;
    }

    synchronized public final void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    public final void setHistory(History h) {
        history = h;
    }

    public final void setMountain(Mountain m) {
        mountain = m;
    }

    public final void setP1(Player p) {
        p1 = p;
    }

    public final void setP2(Player p) {
        p2 = p;
    }

    public final void setPhase(int p) {
        phase = p;
    }

    public final void setPenalty(boolean p) {
        penalty = p;
    }

    public final void setBaseSize(int b) {
        baseSize = b;
    }

    public final void setLastMovePlayed(Move m) {
        lastMovePlayed = m;
    }

    public void setPlayerCase(Player player, Point point, ModelColor color) {
        player.getMountain().setCase(point, color);
    }

    public void setPlayerCase(Player player, int x, int y, ModelColor color) {
        player.getMountain().setCase(x, y, color);
    }

    public void setGameType(int gameType){
        this.gameType = gameType;
    }

    /**********
     * GETTERS
     **********/

    public ArrayList<ModelColor> getBag() {
        return bag;
    }

    synchronized public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public History getHistory() {
        return history;
    }

    public Mountain getMountain() {
        return mountain;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public boolean getPenalty() {
        return penalty;
    }

    public int getBaseSize() {
        return baseSize;
    }

    synchronized public int getPhase() {
        return phase;
    }

    public ModelColor getPlayerCase(Player player, Point point) {
        return player.getMountain().getCase(point);
    }

    public ModelColor getPlayerCase(Player player, int x, int y) {
        return player.getMountain().getCase(x, y);
    }

    public ArrayList<Point> getPlayerRemovable(Player player) {
        return player.getMountain().removable();
    }

    public Move getLastMovePlayed() {
        return lastMovePlayed;
    }

    public Player getPlayerById(int id) {
        if (id == 1) {
            return getP1();
        } else if (id == 2) {
            return getP2();
        } else {
            return null;
        }
    }

    public Player getRandomPlayer() {
        if (new Random().nextInt(2) == 0) {
            return getP1();
        } else {
            return getP2();
        }
    }

    public int getGameType(){
        return gameType;
    }


    /**********
     * PREPARATION PHASE METHODS
     **********/

    /**
     * Fill the bag with nCubePerColor cubes of each color, shuffle the bag util the
     * 9 first cubes have 4 different colors
     * 
     * @return void
     * @throws UnsupportedOperationException if the phase is not the preparation
     *                                       phase
     */
    public void fillBag() throws UnsupportedOperationException {
        fillBag(new Random());
    }

    /**
     * Fill the bag with nCubePerColor cubes of each color, shuffle the bag util the
     * 9 first cubes have 4 different colors
     * 
     * @param random the random object to shuffle the bag
     * @return void
     * @throws UnsupportedOperationException if the phase is not the preparation
     *                                       phase
     */
    public void fillBag(Random random) throws UnsupportedOperationException {

        // Check if the phase is the preparation phase
        if (getPhase() != PREPARATION_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_PREPARATION_PHASE);
        }

        // Fill the bag with nCubePerColor cubes of each color
        bag = new ArrayList<>();
        for (ModelColor c : ModelColor.getAllColored()) {
            for (int i = 0; i < NB_CUBE_PER_COLOR; i++) {
                bag.add(c);
            }
        }
        try {
            // Shuffle the bag until the 9 first cubes have 4 different colors
            while (new HashSet<>(bag.subList(0, 9)).size() < 4) {
                Collections.shuffle(bag, random);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill the bag with nCubePerColor cubes of each color, shuffle the bag util the
     * 9 first cubes have 4 different colors
     * 
     * @param seed the seed to shuffle the bag
     * @return void
     * @throws UnsupportedOperationException if the phase is not the preparation
     *                                       phase
     */
    public void fillBag(int seed) throws UnsupportedOperationException {
        fillBag(new Random(seed));
    }

    /**
     * Fill the base with the 9 first cubes of the bag
     * 
     * @return void
     * @throws UnsupportedOperationException if the phase is not the preparation
     *                                       phase
     */
    public void fillBase() throws UnsupportedOperationException {

        // Check if the phase is the preparation phase
        if (getPhase() != PREPARATION_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_PREPARATION_PHASE);
        }

        // Fill the base with the 9 first cubes of the bag
        for (int y = 0; y < baseSize; y++) {
            mountain.setCase(baseSize - 1, y, bag.remove(0));
        }
    }

    /**
     * Distribute the cubes to the players (2 white, 2 natural and 17 colored cubes
     * to each player)
     * 
     * @return void
     * @throws UnsupportedOperationException if the phase is not the preparation
     *                                       phase
     */
    public void distributeCubesToPlayers() throws UnsupportedOperationException {

        HashMap<ModelColor, Integer> p1Cubes, p2Cubes;
        ModelColor cAvailable;

        // Check if the phase is the preparation phase
        if (getPhase() != PREPARATION_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_PREPARATION_PHASE);
        }

        // Distribute the cubes to the players
        p1Cubes = new HashMap<>();
        p2Cubes = new HashMap<>();

        p1Cubes.put(ModelColor.WHITE, 2);
        p2Cubes.put(ModelColor.WHITE, 2);
        p1Cubes.put(ModelColor.NATURAL, 2);
        p2Cubes.put(ModelColor.NATURAL, 2);

        for (ModelColor c : ModelColor.getAllColored()) {
            p1Cubes.put(c, 0);
            p2Cubes.put(c, 0);
        }

        for (int i = 0; i < 17; i++) {
            cAvailable = bag.remove(0);
            p1Cubes.put(cAvailable, p1Cubes.get(cAvailable) + 1);
            cAvailable = bag.remove(0);
            p2Cubes.put(cAvailable, p2Cubes.get(cAvailable) + 1);
        }

        getP1().setAvailableToBuild(p1Cubes);
        getP2().setAvailableToBuild(p2Cubes);
    }

    /**********
     * GAME PHASE METHODS
     **********/

    /**
     * Check if the given move is playable
     * 
     * @param move the move to check
     * @return true if the move is playable, false otherwise
     * @throws UnsupportedOperationException if the phase is not the
     *                                       game phase
     * @throws IllegalArgumentException      if the move is not a
     *                                       MoveAA, MoveMA,
     *                                       MoveAW, MoveMW, MoveAM
     *                                       or MoveMM
     */
    public boolean isPlayable(Move move) throws UnsupportedOperationException, IllegalArgumentException {

        Player player;
        Player previousPlayer;
        boolean cubeRemovable;
        boolean cubeCompatible;
        ArrayList<ModelColor> additionals;
        boolean accessible, sameColor;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        player = getCurrentPlayer();
        cubeRemovable = false;
        cubeCompatible = false;

        // Get the previous player
        if (player == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        // Catching if the move is a MoveAA (penalty from the previousPlayer's
        // additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {
            // Checking if the cube is in the nextPlayer's additionals
            additionals = previousPlayer.getAdditionals();
            cubeRemovable = additionals.contains(move.getColor());
        }
        // Catching if the move is a MoveMA (penalty from the previousPlayer's
        // mountain)
        else if (move.isToAdditionals()) {
            // Checking if the cube is in the nextPlayer's mountain and if it is the same
            // color
            accessible = getPlayerRemovable(previousPlayer).contains(move.getFrom());
            sameColor = getPlayerCase(previousPlayer, move.getFrom()) == move.getColor();
            cubeRemovable = accessible && sameColor;
        }
        // Catching if the move is a MoveAW or a MoveAM (placing a cube from player's
        // additionals)
        else if (move.isFromAdditionals()) {
            // Checking if the cube is in the player's additionals
            additionals = player.getAdditionals();
            cubeRemovable = additionals.contains(move.getColor());
        }
        // Catching if the move is a MoveMW or an MM (placing from player's mountain)
        else if (move.isWhite() || move.isClassicMove()) {
            // Checking if the cube is in the player's mountain and if it is the same color
            accessible = getPlayerRemovable(player).contains(move.getFrom());
            sameColor = getPlayerCase(player, move.getFrom()) == move.getColor();
            cubeRemovable = accessible && sameColor;
        } else {
            // Should never happen because we are checking all type of the move
            throw new IllegalArgumentException();
        }

        // Catching if the move is a MoveMW or MoveAW (placing a white cube)
        if (move.isWhite()) {
            cubeCompatible = !getPenalty();
        }
        // Catching if the move is a MoveAA or MoveMA (penalty)
        else if (move.isToAdditionals()) {
            // Checking if a penalty is in progress
            cubeCompatible = getPenalty();
        }
        // Catching if the move is a MoveMM or MoveAM (placing on k3)
        else if (move.isFromAdditionals() || move.isClassicMove()) {
            // Checking if the cube is compatible with the base
            cubeCompatible = getMountain().compatible(move.getColor()).contains(move.getTo()) && !getPenalty();
        } else {
            // Should never happen because we are checking all type of the move
            throw new IllegalArgumentException();
        }

        // if the cube is removable and compatible with the base, the move is playable
        return cubeRemovable && cubeCompatible;
    }

    /**
     * Play the given move if it is playable
     * 
     * @param move
     * @return true if the move is played, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    public boolean playMoveWithoutHistory(Move move) throws UnsupportedOperationException {

        Player player;
        Player previousPlayer;
        ModelColor color;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        player = getCurrentPlayer();
        color = move.getColor();

        // Get the previous player
        if (player == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        // Check if the move is playable
        if (!isPlayable(move)) {
            return false;
        }

        // Catching if the move is a MoveAA (penalty from the previousPlayer's
        // additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {
            // Applying the move
            previousPlayer.getAdditionals().remove(color);
            player.addToAdditionals(color);
            setPenalty(false);
        }
        // Catching if the move is a MoveMA (penalty from previousPlayer's mountain)
        else if (move.isToAdditionals() && !move.isFromAdditionals()) {
            // Applying the move
            previousPlayer.removeFromMountain(move.getFrom().x, move.getFrom().y);
            player.addToAdditionals(color);
            setPenalty(false);
        }
        // Catching if the move is a MoveAW (placing a white cube from self additionals)
        else if (move.isWhite() && move.isFromAdditionals()) {
            // Applying the move
            player.getAdditionals().remove(color);
        }
        // Catching if the move is a MoveMW (placing a white cube from player's
        // mountain)
        else if (move.isWhite()) {
            // Applying the move
            player.removeFromMountain(move.getFrom().x, move.getFrom().y);
        }
        // Catching if the move is a MoveAM (Placing a cube from player's additionals on
        // the K3)
        else if (move.isFromAdditionals()) {
            // Applying the move
            player.getAdditionals().remove(color);
            getMountain().setCase(move.getTo().x, move.getTo().y, color);
            // Check whether the move results in a penalty
            if (getMountain().isPenalty(move.getTo())) {
                setPenalty(true);
            }
        }
        // Catching if the move is a MoveMM (Placing a cube from player's mountain on
        // the k3)
        else if (move.isClassicMove()) {
            // Applying the move
            player.removeFromMountain(move.getFrom().x, move.getFrom().y);
            getMountain().setCase(move.getTo().x, move.getTo().y, color);
            // Check whether the move results in a penalty
            if (getMountain().isPenalty(move.getTo())) {
                setPenalty(true);
            }
        }

        // Set the player of the move
        move.setPlayer(player);

        // If the move is not a penalty, set the next player
        if (!move.isToAdditionals()) {
            player.addUsedPiece(move.getColor());
            nextPlayer();
        }
        lastMovePlayed = move;
        return true;
    }

    /**
     * Play the given move if it is playable and add it to the history
     * 
     * @param move the move to play
     * @return true if the move is played, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    public boolean playMove(Move move) throws UnsupportedOperationException {

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        // Play the move
        if (playMoveWithoutHistory(move)) {
            // Add the move to the history
            getHistory().addMove(move);
            return true;
        }

        return false;
    }

    /**
     * Un play the given move without affecting the history
     * 
     * @param move the move to unplay
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    private void unPlayWithoutHistory(Move move) throws UnsupportedOperationException {

        Player player, previousPlayer;
        MoveAA aa;
        MoveMA ma;
        MoveMW mw;
        MoveMM mm;
        MoveAM am;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        player = move.getPlayer();

        // Get the previousPlayer player
        if (player == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        // Catching if the move is a MoveAA (penalty from the previousPlayer's
        // additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {
            // Cancel the move
            aa = (MoveAA) move;
            player.getAdditionals().remove(aa.getColor());
            previousPlayer.addToAdditionals(aa.getColor());
            setPenalty(true);
        }
        // Catching if the move is a MoveMA (penalty from the previousPlayer's
        // mountain)
        else if (move.isToAdditionals() && !move.isFromAdditionals()) {
            // Cancel the move
            ma = (MoveMA) move;
            player.getAdditionals().remove(ma.getColor());
            setPlayerCase(previousPlayer, ma.getFrom(), ma.getColor());
            setPenalty(true);
        }
        // Catching if the move is a MoveAW (placing a white cube from player's
        // additionals)
        else if (move.isWhite() && move.isFromAdditionals()) {
            // Cancel the move
            player.addToAdditionals(ModelColor.WHITE);
        }
        // Catching if the move is a MoveMW (placing a white cube from player's
        // mountain)
        else if (move.isWhite()) {
            // Cancel the move
            mw = (MoveMW) move;
            setPlayerCase(player, mw.getFrom(), mw.getColor());
        }
        // Catching if the move is a MoveAM (placing a cube from player's additionals on
        // the k3)
        else if (move.isFromAdditionals()) {
            // Cancel the move
            am = (MoveAM) move;
            player.addToAdditionals(am.getColor());
            mountain.remove(am.getTo());
            setPenalty(false);
        }
        // Catching if the move is a MoveMM (placing a cube from player's mountain on
        // the k3)
        else if (move.isClassicMove()) {
            // Cancel the move
            mm = (MoveMM) move;
            setPlayerCase(player, mm.getFrom(), mm.getColor());
            mountain.remove(mm.getTo());
            setPenalty(false);
        }

        if (!move.isToAdditionals()) {
            player.removeUsedPiece(move.getColor());
        }
        // Set the next player
        setCurrentPlayer(move.getPlayer());
        lastMovePlayed = move;
    }

    /**
     * Un play the last move and remove it from the history
     * 
     * @return true if the move is unplayed, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    public boolean unPlay() throws UnsupportedOperationException {

        Move m;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        // Un play the last move if there is one
        if (getHistory().canUndo()) {
            m = getHistory().undoMove();
            if (m == null) {
                return false;
            }
            unPlayWithoutHistory(m);
            return true;
        }

        return false;
    }

    /**
     * Re-play the last move that has been unplayed
     * 
     * @return true if the move is replayed, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    public boolean rePlay() throws UnsupportedOperationException {

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        // Re-play the last move that has been unplayed
        if (getHistory().canRedo()) {
            return playMoveWithoutHistory(getHistory().redoMove());
        }

        return false;
    }

    /**
     * Return the list of moves that can be played as a penalty by the
     * currentPlayer
     * 
     * @return the list of moves that can be played as a penalty
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    private ArrayList<Move> penaltySet(Player player) throws UnsupportedOperationException {

        ArrayList<Move> moves;
        Player previousPlayer;
        ModelColor cMountain;
        MoveAA aa;
        MoveMA ma;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        // Get the previousPlayer
        if (player == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        moves = new ArrayList<>();

        // Adding the list of MoveAA
        for (ModelColor c : previousPlayer.getAdditionals()) {
            aa = new MoveAA(c);
            moves.add(aa);
        }

        // Adding the list of MoveMA
        for (Point p : getPlayerRemovable(previousPlayer)) {
            cMountain = getPlayerCase(previousPlayer, p);
            ma = new MoveMA(p, cMountain);
            moves.add(ma);
        }

        return moves;
    }

    /**
     * Return the list of moves that can be played by the currentPlayer
     * 
     * @return the list of moves that can be played
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    synchronized public ArrayList<Move> moveSet() {
        return moveSet(getCurrentPlayer());
    }

    /**
     * Return the list of moves that can be played by the player p
     * 
     * @return the list of moves that can be played
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    synchronized public ArrayList<Move> moveSet(Player p) throws UnsupportedOperationException {

        ModelColor cMountain;
        Move mm, mw, am, aw;
        ArrayList<Move> moves;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        // If a penalty is in progress, return the penalty set
        if (getPenalty()) {
            return penaltySet(p);
        }

        moves = new ArrayList<>();

        // Adding list of MoveMM and MoveMW moves
        for (Point start : getPlayerRemovable(p)) {
            cMountain = getPlayerCase(p, start);
            if (cMountain == ModelColor.WHITE) {
                mw = new MoveMW(start);
                moves.add(mw);
            } else {
                for (Point arr : getMountain().compatible(cMountain)) {
                    mm = new MoveMM(start, arr, cMountain);
                    moves.add(mm);
                }
            }
        }

        // Adding the list AM/AW moves
        for (ModelColor cAdditionals : p.getAdditionals()) {
            if (cAdditionals == ModelColor.WHITE) {
                aw = new MoveAW();
                moves.add(aw);
            } else {
                for (Point arr : getMountain().compatible(cAdditionals)) {
                    am = new MoveAM(arr, cAdditionals);
                    moves.add(am);
                }
            }
        }
        return moves;
    }

    /**
     * Check if the current player can play
     * 
     * @return true if the current player can play, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    public Boolean canCurrentPlayerPlay() throws UnsupportedOperationException {

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException(NOT_IN_GAME_PHASE);
        }

        return (!moveSet().isEmpty());
    }

    /**********
     * OTHER METHODS
     **********/

    /**
     * Changing the current player to the next player
     * 
     * @return void
     */
    public void nextPlayer() {

        if (getCurrentPlayer() == getP1()) {
            setCurrentPlayer(getP2());
        } else {
            setCurrentPlayer(getP1());
        }
    }

    /**
     * Change the current phase to the game phase if the two players have validated
     * 
     * @return the current phase
     */
    public int updatePhase() {

        boolean p1ValidateBuilding, p2ValidateBuilding, isPreparationPhase;

        p1ValidateBuilding = getP1() != null && getP1().getIsMountainValidated();
        p2ValidateBuilding = getP2() != null && getP2().getIsMountainValidated();
        isPreparationPhase = getPhase() == PREPARATION_PHASE;
        if (isPreparationPhase && p1ValidateBuilding && p2ValidateBuilding) {
            setPhase(2);
        } else if (isPreparationPhase && p1ValidateBuilding && !p2ValidateBuilding) {
            setCurrentPlayer(getP2());
        } else if (isPreparationPhase && !p1ValidateBuilding && p2ValidateBuilding) {
            setCurrentPlayer(getP1());
        }

        return getPhase();
    }

    /**
     * Create a move from the given parameters
     * 
     * @param from       the point from where the cube is taken
     * @param playerFrom the player from where the cube is taken
     * @param to         the point where the cube is placed
     * @param playerTo   the player where the cube is placed
     * @param color      the color of the cube
     * @return the move created
     */
    public Move createMove(Point from, Player playerFrom, Point to, Player playerTo, ModelColor color) {
        // Penalty
        if (playerFrom != null && playerTo != null) {
            if (from == null) {
                return new MoveAA(color);
            } else {
                return new MoveMA(from, color);
            }
        }
        // Classic moves
        if (playerFrom != null && playerTo == null) {
            if (from == null) {
                // Move from additionals
                if (color == ModelColor.WHITE) {
                    return new MoveAW();
                } else {
                    return new MoveAM(to, color);
                }
            } else {
                if (color == ModelColor.WHITE) {
                    return new MoveMW(from);
                } else {
                    return new MoveMM(from, to, color);
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {

        Kube k;
        boolean samePlayerOne, samePlayerTwo, sameK3;

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        k = (Kube) o;
        if (getCurrentPlayer().getId() != k.getCurrentPlayer().getId()) {
            return false;
        }

        samePlayerOne = getP1().equals(k.getP1());
        samePlayerTwo = getP2().equals(k.getP2());
        sameK3 = getMountain().equals(k.getMountain());
        return samePlayerOne && samePlayerTwo && sameK3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getP1(), getP2(), getMountain(), getCurrentPlayer());
    }

    @Override
    public Kube clone() {

        Kube kopy;

        kopy = new Kube(true);
        kopy.setHistory(new History());
        kopy.setP1(getP1().clone());
        kopy.setP2(getP2().clone());

        if (getCurrentPlayer() == getP1()) {
            kopy.setCurrentPlayer(kopy.getP1());
        } else {
            kopy.setCurrentPlayer(kopy.getP2());
        }

        kopy.setPenalty(getPenalty());
        kopy.setBag(new ArrayList<>(getBag()));
        kopy.setPhase(getPhase());
        kopy.setMountain(getMountain().clone());
        return kopy;
    }
}