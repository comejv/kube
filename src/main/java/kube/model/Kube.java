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
    private static final int nCubePerColor = 9;
    private static final int baseSize = 9;
    private static final int preparationPhase = 1;
    private static final int gamePhase = 2;

    // Constructor
    public Kube() {
        setK3(new Mountain(getBaseSize()));
        bag = new ArrayList<>();
        p1 = new Player(1);
        p2 = new Player(2);
        history = new History();
        phase = preparationPhase;
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
    
    public History getHistoric() {
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

    //Setters
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

    // Methods
    public boolean isPlayable(Move move) {
        Point from = move.getFrom();
        Point to = move.getTo();
        Color color = move.getColor();
        Player player = getCurrentPlayer();

        if (from == null || to == null || color == null)
            return false;

        if (player.getMountain().removable().contains(from) && player.getMountain().compatible(color).contains(to))
            return true;

        return false;
    }

    // fill the bag with 9 times each colors, and randomize it until the base is valid 
    public void fillBag() {
        if (bag == null) {
            bag = new ArrayList<>();
        }
        for (Color c : Color.getAllColored()) {
            for (int i = 0; i < nCubePerColor; i++) {
                bag.add(c);
            }
        }
        // verificate that there is 4 differents colors in the baseSize first cubes of the bag
        while (new HashSet<>(bag.subList(0, 9)).size() < 4) {
            Collections.shuffle(bag);
        }
    }

    // fill the base with baseSize random colors
    public void fillBase() {
        for (int y = 0; y < baseSize; y++) {
            k3.setCase(baseSize-1, y, bag.remove(0));
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
        p1.setAdditional(p1Cubes);
        p2.setAdditional(p2Cubes);
    }

    public boolean playMove(Move move) {
        if (playMoveWithoutHistory(move)) {
            history.addMove(move);
            return true;
        }
        return false;
    }

    public boolean playMoveWithoutHistory(Move move) {
        // Checking if the move is valid
        if (isPlayable(move)) {
            Point from = move.getFrom();
            Point to = move.getTo();
            Color color = move.getColor();
            Player player = getCurrentPlayer();
            
            // Applying the move
            player.getMountain().remove(from.x, from.y);
            getK3().setCase(to.x, to.y, color);

            // Checks whether the move results in a penalty
            if (player.getMountain().isPenality(to.x, to.y, color)) {
                // TODO: APPLY PENALTY
            }
            if (getK3().isFull()) {
                // TODO: FINISH THE GAME
            }
            return true;
        }
        return false;
    }
}
