package kube.model;

import kube.model.ai.MiniMaxAI;

public class AI extends Player {

    /**********
     * ATTRIBUTES
     **********/

    private MiniMaxAI ai;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the AI
     * 
     * @param id the id of the player
     * @param ai the AI of the player
     * @param k3 the game
     */
    AI(int id, MiniMaxAI ai, Kube k3) {
        super(id);
        ai.setK3(k3);
        ai.setPlayerId(id);
        this.ai = ai;
    }

    /**********
     * SETTER
     **********/

    public void setAI(MiniMaxAI ai) {
        this.ai = ai;
    }

    /**********
     * GETTER
     **********/

    public MiniMaxAI getAI() {
        return ai;
    }

    /**********
     * METHOD
     **********/

    /**
     * Return if the player is an AI
     * 
     * @return true
     */
    @Override
    public boolean isAI() {
        return true;
    }
}
