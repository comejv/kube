package kube.model;

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

    // Constructeurs
    public Kube() {
        k3 = new Mountain(baseSize);
        bag = new ArrayList<>();
        p1 = new Player(1);
        p2 = new Player(2);
        history = new History();
        fillBag();
        fillBase();
    }

    // Getters

    public ArrayList<Color> getBag() {
        return bag;
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
            k3.setCase(baseSize, y, bag.remove(0));
            System.out.println(k3.getCase(0, y));
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



    }
