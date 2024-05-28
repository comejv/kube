package kube.model.action.move;

// Import model class
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
     * Check if the move is to the additionals (penality)
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
}
