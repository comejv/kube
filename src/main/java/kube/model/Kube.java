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

    public void fillBag() {
        if (bag == null) {
            bag = new ArrayList<>();
        }
        for (Color c : Color.getAllColored()) {
            for (int i = 0; i < nCubePerColor; i++) {
                bag.add(c);
            }
        }
        // verificate that there is 4 differents colors in the 9 first cube of the bag
        while (new HashSet<>(bag.subList(0, 9)).size() < 4) {
            Collections.shuffle(bag);
        }
    }

    public void fillBase(){
        for (int y = 0; y < baseSize; y++){
            k3.setCase(0, y, bag.remove(0));
            System.out.println(k3.getCase(0, y));
        }
    }


    public void nextPlayer() {
        if (currentPlayer == p1) {
            currentPlayer = p2;
        } else if (currentPlayer == p2) {
            currentPlayer = p1;
        } else {
            throw new UnsupportedOperationException("Current player is null");
        }
    }

    public History getHistoric() {
        return history;
    }

    public void setP1(Player p) {
        p1 = p;
    }

    public void setP2(Player p) {
        p2 = p;
    }

    public void setHistoric(History h) {
        history = h;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }
}
