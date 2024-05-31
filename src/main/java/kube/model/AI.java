package kube.model;

// Import model class
import kube.model.ai.MiniMaxAI;

// Import java classes
import java.util.ArrayList;
import java.util.HashMap;

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
     * METHODS
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

    @Override
    public AI clone() {
        AI copy;
        copy = new AI(getId(), getAI().clone());
        copy.setAdditionals(new ArrayList<>(getAdditionals()));
        if (!getIsMountainValidated()) {
            copy.setAvailableToBuild(new HashMap<>(getAvailableToBuild()));
        }
        copy.setName(getName());
        copy.setUsedPiece(new HashMap<>(getUsedPiece()));
        copy.setMountain(getMountain().clone());
        copy.setIsMountainValidated(getIsMountainValidated());
        return copy;
    }
}
