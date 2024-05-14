package kube.model;

import kube.model.action.move.*;
import kube.model.ai.abstractAI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class Kube {

    /**********
     * CONSTANTS
     **********/
    public static final int nCubePerColor = 9;
    public static final int PREPARATION_PHASE = 1;
    public static final int GAME_PHASE = 2;

    /**********
     * ATTRIBUTES
     **********/
    private Player p1, p2, currentPlayer;
    private ArrayList<Color> bag;
    private boolean penality;
    private History history;
    private int baseSize;
    private Mountain k3;
    private int phase;

    /**********
     * CONSTRUCTOR
     **********/

    public Kube() {
        init();
    }

    public void init() {
        init(null, null);
    }

    public void init(abstractAI typeAI1) {
        init(typeAI1, null);
    }

    public void init(abstractAI typeAI1, abstractAI typeAI2) {
        setBaseSize(9);
        setPhase(PREPARATION_PHASE);
        setK3(new Mountain(getBaseSize()));
        setBag(new ArrayList<Color>());
        fillBag();
        fillBase();
        setHistory(new History());
        setPenality(false);
        if (typeAI1 != null) {
            setP1(new AI(1, typeAI1, this));
        } else {
            setP1(new Player(1));
        }
        if (typeAI2 != null) {
            setP2(new AI(2, typeAI2, this));
        } else {
            setP2(new Player(1));
        }
        if (new Random().nextInt(2) == 0) {
            setCurrentPlayer(getP1());
        } else {
            setCurrentPlayer(getP2());
        }
        distributeCubesToPlayers();
    }

    /**********
     * GETTERS
     **********/
    public ArrayList<Color> getBag() {
        return bag;
    }

    public Player getCurrentPlayer() {
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

    public Color getPlayerCase(Player player, Point point) {
        return player.getMountain().getCase(point);
    }

    public Color getPlayerCase(Player player, int x, int y) {
        return player.getMountain().getCase(x, y);
    }

    public int getPhase() {
        return phase;
    }

    /**********
     * SETTERS
     **********/
    public void setBag(ArrayList<Color> b) {
        bag = b;
    }

    public void setCurrentPlayer(Player p) {
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

    public int updatePhase() {
        if (phase == PREPARATION_PHASE && getP1() != null && getP1().hasValidateBuilding() && getP2() != null
                && getP2().hasValidateBuilding()) {
            phase = GAME_PHASE;
        }
        return phase;
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
    public void fillBag(Integer seed) throws UnsupportedOperationException {

        // Check if the phase is the preparation phase
        if (getPhase() != PREPARATION_PHASE) {
            throw new UnsupportedOperationException();
        }

        // Fill the bag with nCubePerColor cubes of each color
        bag = new ArrayList<>();
        for (Color c : Color.getAllColored()) {
            for (int i = 0; i < nCubePerColor; i++) {
                bag.add(c);
            }
        }

        // Shuffle the bag until the 9 first cubes have 4 differents colors
        while (new HashSet<>(bag.subList(0, 9)).size() < 4) {
            if (seed != null) {
                Collections.shuffle(bag, new Random(seed));
            } else {
                Collections.shuffle(bag);
            }
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
        fillBag(null);
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
            throw new UnsupportedOperationException();
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

        // Check if the phase is the preparation phase
        if (getPhase() != PREPARATION_PHASE) {
            throw new UnsupportedOperationException();
        }

        // Distribute the cubes to the players
        HashMap<Color, Integer> p1Cubes = new HashMap<>();
        HashMap<Color, Integer> p2Cubes = new HashMap<>();

        p1Cubes.put(Color.WHITE, 2);
        p2Cubes.put(Color.WHITE, 2);
        p1Cubes.put(Color.NATURAL, 2);
        p2Cubes.put(Color.NATURAL, 2);

        for (Color c : Color.getAllColored()) {
            p1Cubes.put(c, 0);
            p2Cubes.put(c, 0);
        }

        Color c;
        for (int i = 0; i < 17; i++) {
            c = bag.remove(0);
            p1Cubes.put(c, p1Cubes.get(c) + 1);
            c = bag.remove(0);
            p2Cubes.put(c, p2Cubes.get(c) + 1);
        }

        p1.setAvalaibleToBuild(p1Cubes);
        p2.setAvalaibleToBuild(p2Cubes);
    }

    /**********
     * GAME PHASE METHODS
     **********/

    /**
     * Check if the given move is playable
     * 
     * @param move the move to check
     * @return true if the move is playable, false otherwise
     * @throws UnsupportedOperationException if the phase is not the game phase
     * @throws IllegalArgumentException      if the move is not a MoveAA, MoveMA,
     *                                       MoveAW, MoveMW, MoveAM or MoveMM
     */
    public boolean isPlayable(Move move) throws UnsupportedOperationException, IllegalArgumentException {

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException();
        }

        Player player = getCurrentPlayer();
        Player previousPlayer = null;
        boolean cubeRemovable = false;
        boolean cubeCompatible = false;
        ArrayList<Color> additionals;
        boolean accessible, sameColor;

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
            accessible = previousPlayer.getMountain().removable().contains(move.getFrom());
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
            accessible = player.getMountain().removable().contains(move.getFrom());
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

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException();
        }

        Player player = getCurrentPlayer();
        Player previousPlayer = null;
        Color color = move.getColor();

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
            player.setWhiteUsed(player.getWhiteUsed() + 1);
        }
        // Catching if the move is a MoveMW (placing a white cube from player's
        // mountain)
        else if (move.isWhite()) {
            // Applying the move
            player.removeFromMountain(move.getFrom().x, move.getFrom().y);
            player.setWhiteUsed(player.getWhiteUsed() + 1);
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
            nextPlayer();
        }

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
            throw new UnsupportedOperationException();
        }

        // Play the move
        if (playMoveWithoutHistory(move)) {
            // Add the move to the history
            history.addMove(move);
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
        Move lastMove;
        MoveAA aa;
        MoveMA ma;
        MoveMW mw;
        MoveMM mm;
        MoveAM am;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException();
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
            previousPlayer.getMountain().setCase(ma.getFrom().x, ma.getFrom().y, ma.getColor());
            setPenality(true);
        }
        // Catching if the move is a MoveAW (placing a white cube from player's
        // additionals)
        else if (move.isWhite() && move.isFromAdditionals()) {
            // Cancel the move
            player.addToAdditionals(Color.WHITE);
            player.setWhiteUsed(player.getWhiteUsed() - 1);
        }
        // Catching if the move is a MoveMW (placing a white cube from player's
        // mountain)
        else if (move.isWhite()) {
            // Cancel the move
            mw = (MoveMW) move;
            player.getMountain().setCase(mw.getFrom().x, mw.getFrom().y, mw.getColor());
            player.setWhiteUsed(player.getWhiteUsed() - 1);
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
            player.getMountain().setCase(mm.getFrom().x, mm.getFrom().y, mm.getColor());
            k3.remove(mm.getTo());
            setPenality(false);
        }

        // Set the next player
        if (!move.isToAdditionals()) {
            if (getHistory().getDone().size() > 0) {
                lastMove = getHistory().getDone().get(getHistory().getDone().size() - 1);
                if (!lastMove.isToAdditionals()) {
                    nextPlayer();
                }
            } else {
                nextPlayer();
            }
        }
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
            throw new UnsupportedOperationException();
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

    public boolean rePlay() throws UnsupportedOperationException {

        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException();
        }

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
    private ArrayList<Move> penalitySet() throws UnsupportedOperationException {

        ArrayList<Move> moves;
        Player previousPlayer;
        Color cMountain;
        MoveAA aa;
        MoveMA ma;
        
        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException();
        }

        // Get the previousPlayer
        if (getCurrentPlayer() == getP1()) {
            previousPlayer = getP2();
        } else {
            previousPlayer = getP1();
        }

        moves = new ArrayList<>();

        // Adding the list of MoveAA
        for (Color c : previousPlayer.getAdditionals()) {
            aa = new MoveAA(c);
            moves.add(aa);
        }

        // Adding the list of MoveMA
        for (Point p : previousPlayer.getMountain().removable()) {
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
    public ArrayList<Move> moveSet() throws UnsupportedOperationException {

        Color cMountain;
        Move mm, mw, am, aw;
        ArrayList<Move> moves;

        // Check if the phase is the game phase
        if (getPhase() != GAME_PHASE) {
            throw new UnsupportedOperationException();
        }

        // If a penality is in progress, return the penality set
        if (getPenality()) {
            return penalitySet();
        }

        moves = new ArrayList<>();

        // Adding list of MoveMM and MoveMW moves
        for (Point start : getCurrentPlayer().getMountain().removable()) {
            cMountain = getPlayerCase(getCurrentPlayer(), start);
            if (cMountain == Color.WHITE) {
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
        for (Color cAdditionals : getCurrentPlayer().getAdditionals()) {
            if (cAdditionals == Color.WHITE) {
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
            throw new UnsupportedOperationException();
        }

        return (moveSet().size() > 0);
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

        Kube Kopy = new Kube();

        Kopy.setP1(getP1().clone());
        Kopy.setP2(getP2().clone());

        if (getCurrentPlayer() == getP1()) {
            Kopy.setCurrentPlayer(Kopy.getP1());
        } else {
            Kopy.setCurrentPlayer(Kopy.getP2());
        }

        Kopy.setPenality(getPenality());
        Kopy.setBag(new ArrayList<>(getBag()));
        Kopy.setPhase(getPhase());
        Kopy.setK3(getK3().clone());
        return Kopy;
    }
}