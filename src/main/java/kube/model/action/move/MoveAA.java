package kube.model.action.move;

import kube.model.Color;

public class MoveAA extends Move {

    /**********
     * CONSTRUCTOR
     **********/

     /**
      * Constructor of the class MoveAM
      * @param color the color of the moved cube
      */
    public MoveAA(Color c) {
        super(c);
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
     * Check if the move is to the additionals (penality)
     * 
     * @return true if the move is to the additionals, false otherwise
     */
    @Override
    public boolean isToAdditionals() {
        return true;
    }

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    @Override
    public String forSave() {
        return "{AA;" + super.forSave() + "}";
    }

    @Override
    public String toString() {
        return "Prendre dans les additionels " +
                getColor().forDisplay();
    }
}
