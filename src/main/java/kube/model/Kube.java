package kube.model;

import java.awt.Point;

// Import model classes
import kube.model.ai.MiniMaxAI;
import kube.model.action.move.Move;
import kube.model.action.move.MoveAA;
import kube.model.action.move.MoveAM;
import kube.model.action.move.MoveAW;
import kube.model.action.move.MoveMA;
import kube.model.action.move.MoveMM;
import kube.model.action.move.MoveMW;

// Import java classes
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class Kube implements Serializable {

    /**********
     * CONSTANTS
     **********/

    public static final int NB_CUBE_PER_COLOR = 9;
    public static final int PREPARATION_PHASE = 1;
    public static final int GAME_PHASE = 2;
    public static final int ID_PLAYER_1 = 1;
    public static final int ID_PLAYER_2 = 2;

    /**********
     * ATTRIBUTES
     **********/

    private Player p1, p2, currentPlayer;
    private ArrayList<ModelColor> bag;
    private boolean penality;
    private History history;
    private int baseSize;
    private Mountain k3;
    private int phase;
    private Move lastMovePlayed;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the Kube
     */
    public Kube() {
        init();
    }

    public Kube(boolean empty) {
        if (!empty) {
            init();
        }
    }

    /**********
     * INITIALIZATION
     **********/

    public final void init() {
        init(null, null, new Random());
    }

    public void init(MiniMaxAI typeAI1) {
        init(typeAI1, null, new Random());
    }

    public void init(MiniMaxAI typeAI1, MiniMaxAI typeAI2) {
        init(typeAI1, typeAI2, new Random());
    }

    public void init(MiniMaxAI typeAI1, MiniMaxAI typeAI2, int seed) {
        init(typeAI1, typeAI2, new Random(seed));
    }

    public void init(MiniMaxAI typeAI1, MiniMaxAI typeAI2, Random r) {

        setBaseSize(9);
        setPhase(PREPARATION_PHASE);
        setK3(new Mountain(getBaseSize()));
        setBag(new ArrayList<>());
        fillBag(r);
        fillBase();
        setHistory(new History());
        setPenality(false);

        if (typeAI1 != null) {
            setP1(new AI(ID_PLAYER_1, typeAI1));
        } else {
            setP1(new Player(1));
        }

        if (typeAI2 != null) {
            setP2(new AI(ID_PLAYER_2, typeAI2));
        } else {
            setP2(new Player(2));
        }

        setCurrentPlayer(getP1());
        distributeCubesToPlayers();
    }

    public void init (Kube k) {

        if (k.getP1() instanceof AI) {
            p1 = new AI(ID_PLAYER_1, ((AI) k.getP1()).getAI());
        } else {
            p1 = new Player(ID_PLAYER_1);
        }

        // p1 = new Player(ID_PLAYER_1);
        p1.setName(new String(k.getP1().getName()));
        p1.setMountain(k.getP1().getMountain().clone());
        p1.setHasValidateBuilding(k.getP1().getHasValidateBuilding());
        p1.setAdditionals(new ArrayList<>(k.getP1().getAdditionals()));
        p1.setAvailableToBuild(new HashMap<>(k.getP1().getAvailableToBuild()));
        p1.setUsedPiece(new HashMap<>(k.getP1().getUsedPiece()));

        if (k.getP2() instanceof AI) {
            p2 = new AI(ID_PLAYER_2, ((AI) k.getP1()).getAI());
        } else {
            p2 = new Player(ID_PLAYER_2);
        }

        p2 = new Player(ID_PLAYER_2);
        p2.setName(new String(k.getP2().getName()));
        p2.setMountain(k.getP2().getMountain().clone());
        p2.setHasValidateBuilding(k.getP2().getHasValidateBuilding());
        p2.setAdditionals(new ArrayList<>(k.getP2().getAdditionals()));
        p2.setAvailableToBuild(new HashMap<>(k.getP2().getAvailableToBuild()));
        p2.setUsedPiece(new HashMap<>(k.getP2().getUsedPiece()));

        if (k.getCurrentPlayer() == k.getP1()) {
            currentPlayer = p1;
        } else {
            currentPlayer = p2;
        }

        bag = new ArrayList<>(k.getBag());

        penality = k.getPenality();

        history = new History();
        history.setDone(new CopyOnWriteArrayList<>(k.getHistory().getDone()));
        history.setUndone(new CopyOnWriteArrayList<>(k.getHistory().getUndone()));

        baseSize = k.getBaseSize();

        k3 = k.getK3().clone();

        phase = k.getPhase();

        lastMovePlayed = k.getLastMovePlayed();
    }

    /**********
     * SETTERS
     **********/

    public void setBag(ArrayList<ModelColor> b) {
        bag = b;
    }

    synchronized public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    public void setHistory(History h) {
        history = h;
    }

    public void setK3(Mountain m) {
        k3 = m;
    }

    public void setP1(Player p) {
        p1 = p;
    }

    public void setP2(Player p) {
        p2 = p;
    }

    public void setPhase(int p) {
        phase = p;
    }

    public void setPenality(boolean p) {
        penality = p;
    }

    public void setBaseSize(int b) {
        baseSize = b;
    }

    public void setPlayerCase(Player player, Point point, ModelColor color) {
        player.getMountain().setCase(point, color);
    }

    public void setPlayerCase(Player player, int x, int y, ModelColor color) {
        player.getMountain().setCase(x, y, color);
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

    public Mountain getK3() {
        return k3;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public boolean getPenality() {
        return penality;
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

    /**********
     * PREPARATION PHASE METHODS
     **********/

    /**
     * Fill the bag with nCubePerColor cubes of each color, shuffle the bag util the
     * 9 first cubes have 4 differents colors
     * 
     * @param seed the seed to shuffle the bag
     * @return void
     * @throws UnsupportedOperationException if the phase is not the preparation
     *                                       phase
     */
    public void fillBag(Random r) throws UnsupportedOperationException {

        // Check if the phase is the preparation phase
        if (getPhase() != PREPARATION_PHASE) {
            throw new UnsupportedOperationException("Forbidden operaation, the Kube isn't in preparation phase");
        }

        // Fill the bag with nCubePerColor cubes of each color
        bag = new ArrayList<>();
        for (ModelColor c : ModelColor.getAllColored()) {
            for (int i = 0; i < NB_CUBE_PER_COLOR; i++) {
                bag.add(c);
            }
        }
        try {
            // Shuffle the bag until the 9 first cubes have 4 differents colors
            while (new HashSet<>(bag.subList(0, 9)).size() < 4) {
                Collections.shuffle(bag, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill the bag with nCubePerColor cubes of each color, shuffle the bag util the
     * 9 first cubes have 4 differents colors
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
     * 9 first cubes have 4 differents colors
     * 
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
            throw new UnsupportedOperationException("Forbidden operaation, the Kube isn't in preparation phase");
        }

        // Fill the base with the 9 first cubes of the bag
        for (int y = 0; y < baseSize; y++) {
            k3.setCase(baseSize - 1, y, bag.remove(0));
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
            throw new UnsupportedOperationException("Forbidden operaation, the Kube isn't in preparation phase");
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

        p1.setAvailableToBuild(p1Cubes);
        p2.setAvailableToBuild(p2Cubes);
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
     * @throws UnsupportedOperationException if the move is not a
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
        }

        player = getCurrentPlayer();
        cubeRemovable = false;
        cubeCompatible = false;

        // Get the premvious player
        if (player == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        // Catching if the move is a MoveAA (penality from the previousPlayer's
        // additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {
            // Checking if the cube is in the nextPlayer's additionals
            additionals = previousPlayer.getAdditionals();
            cubeRemovable = additionals.contains(move.getColor());
        }
        // Catching if the move is a MoveMA (penality from the previousPlayer's
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
        // Catching if the move is a MoveMW or a MM (placing from player's mountain)
        else if (move.isWhite() || move.isClassicMove()) {
            // Checking if the cube is in the player's mountain and if it is the same color
            accessible = getPlayerRemovable(player).contains(move.getFrom());
            sameColor = getPlayerCase(player, move.getFrom()) == move.getColor();
            cubeRemovable = accessible && sameColor;
        } else {
            // Should never happen cause we are checking all type of the move
            throw new IllegalArgumentException();
        }

        // Catching if the move is a MoveMW or MoveAW (placing a white cube)
        if (move.isWhite()) {
            // Allways compatible
            cubeCompatible = true;
        }
        // Catching if the move is a MoveAA or MoveMA (penality)
        else if (move.isToAdditionals()) {
            // Checking if a penality is in progress
            cubeCompatible = getPenality();
        }
        // Catching if the move is a MoveMM or MoveAM (placinf on k3)
        else if (move.isFromAdditionals() || move.isClassicMove()) {
            // Checking if the cube is compatible with the base
            cubeCompatible = getK3().compatible(move.getColor()).contains(move.getTo());
        } else {
            // Should never happen cause we are checking all type of the move
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
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

        // Catching if the move is a MoveAA (penality from the previousPlayer's
        // additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {
            // Applying the move
            previousPlayer.getAdditionals().remove(color);
            player.addToAdditionals(color);
            setPenality(false);
        }
        // Catching if the move is a MoveMA (penality from previousPlayer's mountain)
        else if (move.isToAdditionals() && !move.isFromAdditionals()) {
            // Applying the move
            previousPlayer.removeFromMountain(move.getFrom().x, move.getFrom().y);
            player.addToAdditionals(color);
            setPenality(false);
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
            getK3().setCase(move.getTo().x, move.getTo().y, color);
            // Check whether the move results in a penalty
            if (getK3().isPenality(move.getTo())) {
                setPenality(true);
            }
        }
        // Catching if the move is a MoveMM (Placing a cube from player's mountain on
        // the k3)
        else if (move.isClassicMove()) {
            // Applying the move
            player.removeFromMountain(move.getFrom().x, move.getFrom().y);
            getK3().setCase(move.getTo().x, move.getTo().y, color);
            // Check whether the move results in a penalty
            if (getK3().isPenality(move.getTo())) {
                setPenality(true);
            }
        }

        // Set the player of the move
        move.setPlayer(player);

        // If the move is not a penality, set the next player
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
        }

        player = move.getPlayer();

        // Get the previousPlayer player
        if (player == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        // Catching if the move is a MoveAA (penality from the previousPlayer's
        // additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {
            // Cancel the move
            aa = (MoveAA) move;
            player.getAdditionals().remove(aa.getColor());
            previousPlayer.addToAdditionals(aa.getColor());
            setPenality(true);
        }
        // Catching if the move is a MoveMA (penality from the previousPlayer's
        // mountain)
        else if (move.isToAdditionals() && !move.isFromAdditionals()) {
            // Cancel the move
            ma = (MoveMA) move;
            player.getAdditionals().remove(ma.getColor());
            setPlayerCase(previousPlayer, ma.getFrom(), ma.getColor());
            setPenality(true);
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
            k3.remove(am.getTo());
            setPenality(false);
        }
        // Catching if the move is a MoveMM (placing a cube from player's mountain on
        // the k3)
        else if (move.isClassicMove()) {
            // Cancel the move
            mm = (MoveMM) move;
            setPlayerCase(player, mm.getFrom(), mm.getColor());
            k3.remove(mm.getTo());
            setPenality(false);
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
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
     * Re play the last move that has been unplayed
     * 
     * @return true if the move is replayed, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    public boolean rePlay() throws UnsupportedOperationException {

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
        }

        // Re play the last move that has been unplayed
        if (getHistory().canRedo()) {
            return playMoveWithoutHistory(getHistory().redoMove());
        }

        return false;
    }

    /**
     * Return the list of moves that can be played as a penality by the
     * currentPlayer
     * 
     * @return the list of moves that can be played as a penality
     * @throws UnsupportedOperationException if the phase is not the game phase
     */
    private ArrayList<Move> penalitySet(Player player) throws UnsupportedOperationException {

        ArrayList<Move> moves;
        Player previousPlayer;
        ModelColor cMountain;
        MoveAA aa;
        MoveMA ma;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
        }

        // If a penality is in progress, return the penality set
        if (getPenality()) {
            return penalitySet(p);
        }

        moves = new ArrayList<>();

        // Adding list of MoveMM and MoveMW moves
        for (Point start : getPlayerRemovable(p)) {
            cMountain = getPlayerCase(p, start);
            if (cMountain == ModelColor.WHITE) {
                mw = new MoveMW(start);
                moves.add(mw);
            } else {
                for (Point arr : getK3().compatible(cMountain)) {
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
                for (Point arr : getK3().compatible(cAdditionals)) {
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
            throw new UnsupportedOperationException("Forbidden operation, the Kube isn't in game phase");
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

        p1ValidateBuilding = getP1() != null && getP1().getHasValidateBuilding();
        p2ValidateBuilding = getP2() != null && getP2().getHasValidateBuilding();
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

    public Move createMove(Point posFrom, Player playerFrom, Point posTo, Player playerTo, ModelColor color){
        // Penality 
        if (playerFrom != null && playerTo != null){
            if (posFrom == null){
                return new MoveAA(color);
            } else {
                return new MoveMA(posFrom, color);
            }
        }
        // Classic moves
        if (playerFrom != null && playerTo == null){
            if (posFrom == null){
                // Move from additionnals
                if (color == ModelColor.WHITE){
                    return new MoveAW();
                } else {
                    return new MoveAM(posTo, color);
                }
            } else {
                if (color == ModelColor.WHITE){
                    return new MoveMW(posFrom);
                } else {
                    return new MoveMM(posFrom, posTo, color);
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
        sameK3 = getK3().equals(k.getK3());
        return samePlayerOne && samePlayerTwo && sameK3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getP1(), getP2(), getK3(), getCurrentPlayer());
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

        kopy.setPenality(getPenality());
        kopy.setBag(new ArrayList<>(getBag()));
        kopy.setPhase(getPhase());
        kopy.setK3(getK3().clone());
        return kopy;
    }
}
