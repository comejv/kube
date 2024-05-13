package kube.model;

import kube.configuration.Config;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

import kube.model.move.*;

public class Kube {

    private History history;
    private Player p1, p2, currentPlayer;
    private Mountain k3;
    private ArrayList<Color> bag;
    private int phase;
    private boolean penality;
    private int baseSize;
    private static final int nCubePerColor = 9;
    private static final int preparationPhase = 1;
    private static final int gamePhase = 2;

    // Constructor
    public Kube() {

        setBaseSize(9);
        setK3(new Mountain(getBaseSize()));
        setBag(new ArrayList<Color>());
        setP1(new Player(1));
        setP2(new Player(2));
        setHistory(new History());
        setPhase(preparationPhase);
        setPenality(false);
        if (new Random().nextInt(2) == 0){
            setCurrentPlayer(getP1());
        } else {
            setCurrentPlayer(getP2());
        }

    }

    // Getters
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

    public int getPhase() {
        return phase;
    }

    public boolean getPenality() {
        return penality;
    }

    public int getBaseSize() {
        return baseSize;
    }

    // Setters
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

    // Methods
    public boolean isPlayable(Move move) {

        Player player = getCurrentPlayer();
        Player nextPlayer = null;
        boolean cubeRemovable = false;
        boolean cubeCompatible = false;

        if (player == getP1()) {

            nextPlayer = getP2();
        } else {

            nextPlayer = getP1();
        }

        // Catching if the move is a MoveAA (Penality where the player take in
        // oppenent's additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {

            cubeRemovable = nextPlayer.getAdditionals().contains(move.getColor());
        }
        // Catching if the move is a MoveMA (Penality where the player take in
        // oppenent's mountain)
        else if (move.isToAdditionals()) {

            cubeRemovable = nextPlayer.getMountain().removable().contains(move.getFrom()) &&
                    nextPlayer.getMountain().getCase(move.getFrom()) == move.getColor();
        }
        // Catching if the move is a MoveAW (Placing a white cube from self additionals)
        else if (move.isFromAdditionals()) {

            cubeRemovable = player.getAdditionals().contains(move.getColor());
        }
        // Catching if the move is a MoveMW or a MM (Placing a cube from self mountain)
        else if (move.isWhite() || move.isClassicMove()) {

            cubeRemovable = player.getMountain().removable().contains(move.getFrom()) &&
                    player.getMountain().getCase(move.getFrom()) == move.getColor();
        } else {
            // Should never happen
            return false;
        }

        // Catching if the move is a MoveMW or MoveAW
        if (move.isWhite()) {

            // White cube is always compatible
            cubeCompatible = true;
        }
        // Catching if the move is a MoveAA or MoveMA
        else if (move.isToAdditionals()) {

            cubeCompatible = getPenality();
        }
        // Catching if the move is a MoveMM or MoveAM
        else if (move.isFromAdditionals() || move.isClassicMove()) {

            // Checking if the cube is compatible with the base
            cubeCompatible = getK3().compatible(move.getColor()).contains(move.getTo());
        } else {
            // Should never happen
            return false;
        }

        return cubeRemovable && cubeCompatible;
    }

    public void fillBag() {

        fillBag(null);
    }

    // fill the bag with 9 times each colors, and randomize it until the base is
    // valid
    public void fillBag(Integer seed) {

        bag = new ArrayList<>();
        for (Color c : Color.getAllColored()) {

            for (int i = 0; i < nCubePerColor; i++) {

                bag.add(c);
            }
        }
        // verificate that there is 4 differents colors in the baseSize first cubes of
        // the bag
        while (new HashSet<>(bag.subList(0, 9)).size() < 4) {

            if (seed != null) {

                Collections.shuffle(bag, new Random(seed));
            } else {

                Collections.shuffle(bag);
            }
        }
    }

    // fill the base with baseSize random colors
    public void fillBase() {

        for (int y = 0; y < baseSize; y++) {

            k3.setCase(baseSize - 1, y, bag.remove(0));
        }
    }

    // Set current player to the next
    public void nextPlayer() {

        if (currentPlayer == p1) {

            currentPlayer = p2;
        } else if (currentPlayer == p2) {

            currentPlayer = p1;
        } else {

            throw new UnsupportedOperationException("Current player is null");
        }
    }

    public void distributeCubesToPlayers() {

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

    public boolean unPlay() {

        if (getHistory().canUndo()) {

            Move c = getHistory().undoMove();
            if (c == null) {

                return false;
            }
            evomYalp(c);
            return true;
        }
        return false;
    }

    public boolean rePlay() {

        if (getHistory().canRedo()) {

            return playMoveWithoutHistory(getHistory().redoMove());
        }
        return false;
    }

    private void evomYalp(Move move) {

        Player player = move.getPlayer();
        Player nextPlayer;
        // Get the other player
        if (player == getP1()) {

            nextPlayer = getP2();
        } else {

            nextPlayer = getP1();
        }

        // MoveAA
        if (move.isToAdditionals() && move.isFromAdditionals()) {

            MoveAA aa = (MoveAA) move;
            player.getAdditionals().remove(aa.getColor());
            nextPlayer.addToAdditionals(aa.getColor());
            setPenality(true);
        }
        // MoveMA
        else if (move.isToAdditionals() && !move.isFromAdditionals()) {

            MoveMA ma = (MoveMA) move;
            player.getAdditionals().remove(ma.getColor());
            nextPlayer.getMountain().setCase(ma.getFrom().x, ma.getFrom().y, ma.getColor());
            setPenality(true);
        }
        // MoveAW
        else if (move.isWhite() && move.isFromAdditionals()) {

            // MoveAW aw = (MoveAW) move;
            player.addToAdditionals(Color.WHITE);
            player.setWhiteUsed(player.getWhiteUsed() - 1);
        }
        // MoveMW
        else if (move.isWhite()) {

            MoveMW mw = (MoveMW) move;
            player.getMountain().setCase(mw.getFrom().x, mw.getFrom().y, mw.getColor());
            player.setWhiteUsed(player.getWhiteUsed() - 1);
        }
        // moveAM
        else if (move.isFromAdditionals()) {

            MoveAM am = (MoveAM) move;
            player.addToAdditionals(am.getColor());
            k3.remove(am.getTo());
        }
        // MoveMM
        else if (move.isClassicMove()) {

            MoveMM mm = (MoveMM) move;
            player.getMountain().setCase(mm.getFrom().x, mm.getFrom().y, mm.getColor());
            k3.remove(mm.getTo());
        }
        // Penality doesn't change the current player
        if (!move.isToAdditionals()) {
            try {
                if (!getHistory().getDone().get(getHistory().getDone().size() - 1).isToAdditionals()) {
                    nextPlayer();
                }
            }
            catch (IndexOutOfBoundsException e) {
                nextPlayer();
            }
        }
    }

    public boolean playMove(Move move) {

        if (playMoveWithoutHistory(move)) {

            history.addMove(move);
            return true;
        }
        return false;
    }

    public boolean playMoveWithoutHistory(Move move) {

        Player player = getCurrentPlayer();
        Player nextPlayer = null;

        if (player == getP1()) {
            nextPlayer = getP2();
        } else {
            nextPlayer = getP1();
        }

        Color color = move.getColor();

        // Checking if the move is playable
        if (!isPlayable(move)) {
            return false;
        }

        // Catching if the move is a MoveAA (Penality where the player take in
        // oppenent's additionals)
        if (move.isToAdditionals() && move.isFromAdditionals()) {

            // Getting out the additional cube from the player's additional cubes
            nextPlayer.getAdditionals().remove(color);
            // Adding the additional cube to the player's mountain
            player.addToAdditionals(color);
            setPenality(false);
        }
        // Catching if the move is a MoveMA (Penality where the player take in
        // oppenent's mountain)
        else if (move.isToAdditionals() && !move.isFromAdditionals()) {

            // Getting out the additional cube from the player's mountain
            nextPlayer.removeFromMountain(move.getFrom().x, move.getFrom().y);
            // Adding the additional cube to the player's additional cubes
            player.addToAdditionals(color);
            setPenality(false);
        }
        // Catching if the move is a MoveAW (Placing a white cube from self additionals)
        else if (move.isWhite() && move.isFromAdditionals()) {

            // Getting out the additional white cube from the player's additional cubes
            player.getAdditionals().remove(color);
            // Adding the white cube to the player's used white cubes
            player.setWhiteUsed(player.getWhiteUsed() + 1);
        }
        // Catching if the move is a MoveMW (Placing a white cube from self mountain)
        else if (move.isWhite()) {

            // Getting out the white cube from the player's mountain
            player.removeFromMountain(move.getFrom().x, move.getFrom().y);
            // Adding the white cube to the player's used white cubes
            player.setWhiteUsed(player.getWhiteUsed() + 1);
        }
        // Catching if the move is a MoveAM (Placing a cube from self additionals on the
        // K3)
        else if (move.isFromAdditionals()) {

            // Getting out the additional cube from the player's additional cubes
            player.getAdditionals().remove(color);
            // Adding the additional cube to the player's mountain
            getK3().setCase(move.getTo().x, move.getTo().y, color);
            // Checks whether the move results in a penalty
            if (getK3().isPenality(move.getTo())) {
                setPenality(true);
            }
        }
        // Catching if the move is a MoveMM (Placing a cube from self mountain on the
        // K3)
        else if (move.isClassicMove()) {

            // Applying the move
            player.removeFromMountain(move.getFrom().x, move.getFrom().y);
            getK3().setCase(move.getTo().x, move.getTo().y, color);
            // Checks whether the move results in a penalty
            if (getK3().isPenality(move.getTo())) {
                setPenality(true);
            }
        }
        move.setPlayer(player);
        if (!move.isToAdditionals()) {
            nextPlayer();
        }
        return true;
    }

    public Boolean canCurrentPlayerPlay() {
        return (moveSet().size() > 0);
    }

    // Method that return the list of moves available for the current player
    public ArrayList<Move> moveSet() {

        if (getPenality()) {
            return penalitySet();
        }

        ArrayList<Move> moves = new ArrayList<>();
        // List MM/MW moves
        for (Point start : getCurrentPlayer().getMountain().removable()) {
            Color c = getCurrentPlayer().getMountain().getCase(start);
            if (c == Color.WHITE) {
                Move mw = new MoveMW(start);
                moves.add(mw);
            } else {
                for (Point arr : getK3().compatible(c)) {
                    Move mm = new MoveMM(start, arr, c);
                    moves.add(mm);
                }
            }
        }
        // List AM/AW moves
        for (Color c : getCurrentPlayer().getAdditionals()) {
            if (c == Color.WHITE) {
                Move aw = new MoveAW();
                moves.add(aw);
            } else {
                for (Point arr : getK3().compatible(c)) {
                    Move am = new MoveAM(arr, c);
                    moves.add(am);
                }
            }
        }
        return moves;
    }

    private ArrayList<Move> penalitySet() {
        ArrayList<Move> moves = new ArrayList<>();
        Player nextPlayer;
        if (getCurrentPlayer() == getP1()) {
            nextPlayer = getP2();
        } else {
            nextPlayer = getP1();
        }
        // List AA
        for (Color c : nextPlayer.getAdditionals()) {
            MoveAA aa = new MoveAA(c);
            moves.add(aa);
        }
        // List MA
        for (Point p : nextPlayer.getMountain().removable()) {
            Color c = nextPlayer.getMountain().getCase(p);
            MoveMA ma = new MoveMA(p, c);
            moves.add(ma);
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Kube k = (Kube) o;
        if (getCurrentPlayer().getId() != k.getCurrentPlayer().getId()){
            return false;
        } 
        return getP1().equals(k.getP1()) && getP2().equals(k.getP2()) && getK3().equals(k.getK3());
    }

    @Override 
    public int hashCode(){
        return Objects.hash(getP1(), getP2(), getK3(), getCurrentPlayer());
    }

    @Override
    public Kube clone(){
        Kube Kopy = new Kube();
        Kopy.setP1(getP1().clone());
        Kopy.setP2(getP2().clone());
        if (getCurrentPlayer() == getP1()){
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
