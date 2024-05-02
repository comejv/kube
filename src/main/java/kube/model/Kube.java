package kube.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Kube {
    private History history;
    private Player p1, p2, currentPlayer;
    private Mountain k3;
    private ArrayList<Color> bag;
    private int phase;
    private boolean penality;
    private static final int nCubePerColor = 9;
    private static final int baseSize = 9;
    private static final int preparationPhase = 1;
    private static final int gamePhase = 2;

    // Constructor
    public Kube() {
        setK3(new Mountain(getBaseSize()));
        setBag(new ArrayList<Color>());
        setP1(new Player(1));
        setP2(new Player(2));
        setHistoric(new History());
        setPhase(preparationPhase);
        setPenality(false);
    }

    // Getters
    public ArrayList<Color> getBag() {
        return bag;
    }

    public int getBaseSize() {
        return baseSize;
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

    // Setters
    public void setBag(ArrayList<Color> b) {
        bag = b;
    }

    public int setBaseSize() {
        return baseSize;
    }

    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    public void setHistoric(History h) {
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
    
    // Methods
    public boolean isPlayable(Move move) {

        Player player = getCurrentPlayer();
        boolean cubeRemovable = false;
        boolean cubeCompatible = false;

        if (move.isWhite() || move.isClassicMove()) {
            // When we take the cube from the player's mountain, checking if the cube is
            // removable
            cubeRemovable = player.getMountain().removable().contains(move.getFrom());
        } else if (move.isFromAdditionals()) {
            // When we take the cube from the player's additional cubes, checking if the
            // move's cube color is in
            cubeRemovable = player.getAdditional().contains(move.getColor());
        } else {
            // Should never happen
            return false;
        }

        if (move.isFromAdditionals() || move.isClassicMove()) {
            // Checking if the cube is compatible with the base
            cubeCompatible = getK3().compatible(move.getColor()).contains(move.getTo());
        } else if (move.isWhite()) {
            // White cube is always compatible
            cubeCompatible = true;
        } else {
            // Should never happen
            return false;
        }

        return cubeRemovable && cubeCompatible;
    }

    // fill the bag with 9 times each colors, and randomize it until the base is
    // valid
    public void fillBag() {
        bag = new ArrayList<>();
        for (Color c : Color.getAllColored()) {
            for (int i = 0; i < nCubePerColor; i++) {
                bag.add(c);
            }
        }
        // verificate that there is 4 differents colors in the baseSize first cubes of
        // the bag
        while (new HashSet<>(bag.subList(0, 9)).size() < 4) {
            Collections.shuffle(bag);
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
        ArrayList<Color> p1Cubes = new ArrayList<>();
        ArrayList<Color> p2Cubes = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            p1Cubes.add(bag.remove(0));
            p2Cubes.add(bag.remove(0));
        }
        for (int i = 0; i < 2; i++) {
            p1Cubes.add(Color.WHITE);
            p1Cubes.add(Color.NATURAL);
            p2Cubes.add(Color.WHITE);
            p2Cubes.add(Color.NATURAL);
        }
        p1Cubes.sort(Color.compareByValue);
        p2Cubes.sort(Color.compareByValue);
        p1.setAvalaibleToBuild(p1Cubes);
        p2.setAvalaibleToBuild(p2Cubes);
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
        Color color = move.getColor();
        boolean result = false;

        // Catching if the move is about a colored cube or a white cube
        if (move.isWhite() && isPlayable(move)) {
            // Getting out the white cube from the player's mountain
            player.getMountain().remove(move.getFrom().x, move.getFrom().y);

            // Adding the white cube to the player's used white cubes
            player.setWhiteUsed(player.getWhiteUsed() + 1);
            result = true;
        }
        // Checking if the move is about an additional cube
        else if (move.isFromAdditionals() && isPlayable(move)) {

            // Getting out the additional cube from the player's additional cubes
            player.getAdditional().remove(color);

            // Adding the additional cube to the player's mountain
            getK3().setCase(move.getTo().x, move.getTo().y, color);

            // Checks whether the move results in a penalty
            if (player.getMountain().isPenality(move.getTo().x, move.getTo().y, color)) {
                setPenality(true);
            }

            result = true;
        }
        // Checking if the move is a classic move and is playable
        else if (move.isClassicMove() && isPlayable(move)) {

            // Applying the move
            player.getMountain().remove(move.getFrom().x, move.getFrom().y);
            getK3().setCase(move.getTo().x, move.getTo().y, color);

            // Checks whether the move results in a penalty
            if (player.getMountain().isPenality(move.getTo().x, move.getTo().y, color)) {
                setPenality(true);
            }

            result = true;
        }

        if (result) {
            nextPlayer();
        }
        return result;
    }

    public Boolean canCurrentPlayerPlay(){
        if (getCurrentPlayer().getPlayableColors().size() == 0){
            return false;
        } else {
            return true;
        }
    }
}
