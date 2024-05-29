package kube.model;

import java.util.ArrayList;
import java.util.HashMap;

import kube.model.ai.MiniMaxAI;

public class AI extends Player {

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the AI
     * 
     * @param id the id of the player
     * @param ai the AI of the player
     */
    public AI(int id, MiniMaxAI ai) {
        super(id);
        ai.setPlayerId(id);
        setAI(ai);
        setName("AI" + id);
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

    public AI clone() {
        AI copy = new AI(getId(), getAI().clone());
        copy.setAdditionals(new ArrayList<>(getAdditionals()));
        if (!getHasValidateBuilding()) {
            copy.setAvailableToBuild(new HashMap<>(getAvailableToBuild()));
        }
        copy.setName(getName());
        copy.setUsedPiece(new HashMap<>(getUsedPiece()));
        copy.setMountain(getMountain().clone());
        copy.setHasValidateBuilding(getHasValidateBuilding());
        return copy;
    }
}
