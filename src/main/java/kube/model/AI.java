package kube.model;

import kube.model.ai.abstractAI;

public class AI extends Player {

    /**********
     * ATTRIBUTES
     **********/
    private abstractAI ai;

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
    AI(int id, abstractAI ai, Kube k3){
        super(id);
        ai.setK3(k3);
        ai.setPlayer(this);
        setAI(ai);
    }

    /**********
     * SETTER
     **********/
    public void setAI(abstractAI ai){
        this.ai = ai;
    }

    /**********
     * GETTER
     **********/
    public abstractAI getAI(){
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
    public boolean isAI(){
        return true;
    }
}
