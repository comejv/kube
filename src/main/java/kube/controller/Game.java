package kube.controller;

import java.util.ArrayList;
import java.util.Random;

import kube.model.*;
import kube.model.ai.*;
import kube.model.move.*;

public class Game {

    private Kube kube;
    private int nbPlayers;
    private AI ai;
    private AI ai2;

    public Game(int nbPlayers) {
        setKube(new Kube());
        setNbPlayers(nbPlayers);
        initGame();
    }

    // Getters

    public Kube getKube() {
        return this.kube;
    }

    public int getNbPlayers() {
        return this.nbPlayers;
    }

    public AI getAI() {
        return this.ai;
    }

    public AI getAI2() {
        return this.ai2;
    }

    // Setters
    public void setKube(Kube kube) {
        this.kube = kube;
    }

    public void setNbPlayers(int nbPlayers) {
        this.nbPlayers = nbPlayers;
    }

    public void setAI(AI ai) {
        this.ai = ai;
    }

    public void setAI2(AI ai2) {
        this.ai2 = ai2;
    }

    // Methods
    public void initGame() {
        getKube().fillBag();
        getKube().fillBase();
        getKube().distributeCubesToPlayers();

        if (getNbPlayers() < 0 || getNbPlayers() > 2) {
            throw new IllegalArgumentException("The number of players must be 1 or 2");
        }

        if (getNbPlayers() == 0) {
            setAI(new RandomAI(kube, kube.getP1()));
            setAI2(new RandomAI(kube, kube.getP2()));
            getAI().preparationPhase();
            getAI2().preparationPhase();
        }

        if (getNbPlayers() == 1) {
            setAI(new RandomAI(kube, kube.getP2()));
            getAI().preparationPhase();
        }

        randomizeMoutain(kube.getP1());
        randomizeMoutain(kube.getP2());

    }

    public void randomizeMoutain() {
        randomizeMoutain(getKube().getCurrentPlayer());
    }

    public void randomizeMoutain(Player p) {
        utilsAI.randomFillMoutain(p, new Random());
    }

    public ArrayList<Move> moveSet() {
        return getKube().moveSet();
    }

    public boolean playMove(int i) {
        boolean res;
        try {
            res = getKube().playMove(moveSet().get(i));
        } catch (Exception e) { // In case of Index Out of Bound Exception
            res = false;
        }
        return res;
    }

    public String listMove() {
        String res = "";
        for (int i = 0; i < moveSet().size(); i++) {
            res += i + " : " + moveSet().get(i).toString() + "\n";
        }
        return res;
    }

    public boolean isOver() {
        return !getKube().canCurrentPlayerPlay();
    }

    public String printMountain(Player p) {
        return p.getMountain().toString();
    }

    public String printK3() {
        return getKube().getK3().toString();
    }

    public void swap(int x1, int y1, int x2, int y2) {
        Color col = getKube().getCurrentPlayer().getMountain().getCase(x1, y1);
        getKube().getCurrentPlayer().getMountain().setCase(x1, y1,
                getKube().getCurrentPlayer().getMountain().getCase(x2, y2));
        getKube().getCurrentPlayer().getMountain().setCase(x2, y2, col);
    }

    public void nextPlayer() {
        getKube().nextPlayer();
    }
}
