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

    public Player getCurrentPlayer() {
        return getKube().getCurrentPlayer();
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

    }

    public boolean isOver() {
        return !getKube().canCurrentPlayerPlay();
    }

    public String listMove() {
        String res = "";
        for (int i = 0; i < moveSet().size(); i++) {
            res += i + " : " + moveSet().get(i).toString() + "\n";
        }
        return res;
    }

    public ArrayList<Move> moveSet() {
        return getKube().moveSet();
    }

    public void nextPlayer() {
        getKube().nextPlayer();
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

    public String printK3() {
        return getKube().getK3().toString();
    }

    public String printMountain(Player p) {
        return p.getMountain().toString();
    }

    public void randomizeMoutain() {
        randomizeMoutain(getKube().getCurrentPlayer());
    }

    public void randomizeMoutain(Player p) {
        utilsAI.randomFillMountain(p, new Random());
    }



    public String swap(int x1, int y1, int x2, int y2) {
        String s = "";
        if (x1 < 0 || x1 > 5 || y1 < 0 || y1 > x1 || x2 < 0 || x2 > 5 || y2 < 0 || y2 > x2) {
            return "Invalid coordinates";
        }
        s += "Swap (" + getKube().getCurrentPlayer().getMountain().getCase(x1, y1).forDisplay() + ") and ("
                + getKube().getCurrentPlayer().getMountain().getCase(x2, y2).forDisplay() + ")\n";
        Color col = getKube().getCurrentPlayer().getMountain().getCase(x1, y1);
        getKube().getCurrentPlayer().getMountain().setCase(x1, y1,
                getKube().getCurrentPlayer().getMountain().getCase(x2, y2));
        getKube().getCurrentPlayer().getMountain().setCase(x2, y2, col);
        return s;
    }

    public void setPhase(int phase){
        getKube().setPhase(phase);
    }

    public void undo() {
        getKube().unPlay();
    }
    
    public void redo() {
        getKube().rePlay();
    }

}
