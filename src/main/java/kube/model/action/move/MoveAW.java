package kube.model.action.move;

// Import model class
import kube.model.ModelColor;
// Import jackson class
import com.fasterxml.jackson.annotation.JsonCreator;

public class MoveAW extends Move {

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class MoveAM
     */
    @JsonCreator
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

    @Override
    public String toString() {
        return "Passer son tour depuis les additionels " +
                getColor().forDisplay();
    }
}
