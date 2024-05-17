package kube.model.action.move;

import kube.model.ModelColor;

public class MoveAW extends Move {

    /**********
     * CONSTRUCTOR
     **********/

     /**
      * Constructor of the class MoveAM
      */
    public MoveAW() {
        super(ModelColor.WHITE);
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
     * Check if the move is a white move
     * 
     * @return true if the move is a white move, false otherwise
     */
    @Override
    public boolean isWhite() {
        return true;
    }

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    @Override
    public String forSave() {
        return "{AW}";
    }

    @Override
    public String toString() {
        return "Passer son tour depuis les additionels " +
                getColor().forDisplay();
    }
}
