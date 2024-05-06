package kube.controler;

import kube.model.*;
import kube.model.ai.AI;
import kube.model.ai.RandomAI;
import kube.model.move.*;

public class Game {

    private Kube kube;
    private int nbPlayers;
    private AI ai;
    private AI ai2;

    private Game(int nbPlayers) {
        setKube(new Kube());
        setNbPlayers(nbPlayers);
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

    public void randomizeMoutain(Player p) {
        Mountain m = p.getMountain();
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j < m.getBaseSize(); j++) {
                p.getAvalaibleToBuild().keySet();
                Color randomKey = null;
                int amount = 0;
                while (amount == 0) {
                    while ((randomKey = p.getAvalaibleToBuild().keySet().stream().findAny().orElse(null)) == null)
                        ;
                    if ((amount = p.getAvalaibleToBuild().get(randomKey)) > 0) {
                        m.setCase(i, j, randomKey);
                        p.getAvalaibleToBuild().put(randomKey, p.getAvalaibleToBuild().get(randomKey) - 1);
                    }
                }
            }
        }
    }


    //For Whites
    public boolean playMove(int l,int c){
        Color color = getKube().getCurrentPlayer().getMountain().getCase(l, c);
        if (color == Color.EMPTY) {
            return false;
        }
        else if (color == Color.WHITE){
            return getKube().playMove(new MoveMW(l,c));
        }

        return false;
    }

    //Basic case
    public boolean playMove(int l,int c,int tl, int tc){
        return getKube().playMove(new MoveMM(l,c,tl,tc, getKube().getCurrentPlayer().getMountain().getCase(l, c)));
    }

    //For Additionals To Additionals
    public boolean playMove(Color color){
        return getKube().playMove(new MoveAA(color));
    }

    //For Additionals To Mountain
    public boolean playMove(int l,int c,Color color){
        return getKube().playMove(new MoveAM(l,c,color));
    }






}
