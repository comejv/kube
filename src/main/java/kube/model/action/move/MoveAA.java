package kube.model.action.move;

import kube.model.ModelColor;

public class MoveAA extends Move {

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class MoveAM
     * 
     * @param color the color of the moved cube
     */
    public MoveAA(ModelColor color) {
        super(color);
    }

    /**********
     * METHODS
     **********/

    /**
     * Check if the move is from the additionals
     *
     * @return true if the move is from the additionals, false otherwise
     */
    @Override
    public boolean isFromAdditionals() {
        return true;
    }

    /**
     * Check if the move is to the additionals (penalty)
     * 
     * @return true if the move is to the additionals, false otherwise
     */
    @Override
    public boolean isToAdditionals() {
        return true;
    }

    @Override
    public String toString() {
        return "Prendre dans les additionels " +
                getColor().forDisplay();
    }

    @Override
    public String toHTML() {
        return "<b>" + getPlayer().getName() + "</b>" + " prends dans les additionels " +
                getColor().forDisplayHTML();
    }
}
