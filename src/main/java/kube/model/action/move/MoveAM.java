package kube.model.action.move;

// Import model class
import kube.model.ModelColor;
// Import jackson classes
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
// Import java class
import java.awt.Point;

public class MoveAM extends Move {

    /**********
     * ATTRIBUTE
     **********/

    @JsonProperty("to")
    private Point to;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class MoveAM
     *
     * @param to    the destination of the move
     * @param color the color of the moved cube
     */
    @JsonCreator
    public MoveAM(@JsonProperty("to") Point to, @JsonProperty("color") ModelColor color) {
        super(color);
        this.to = to;
    }

    /**
     * Constructor of the class MoveAM
     * 
     * @param toX   the x position of the destination of the move
     * @param toY   the y position of the destination of the move
     * @param color the color of the moved cube
     */
    public MoveAM(int toX, int toY, ModelColor color) {
        this(new Point(toX, toY), color);
    }

    /**********
     * SETTERS
     **********/

    @JsonSetter("to")
    public void setTo(Point to) {
        this.to = to;
    }

    public void setTo(int toX, int toY) {
        setTo(new Point(toX, toY));
    }

    /**********
     * GETTER
     **********/

    @JsonGetter("to")
    public Point getTo() {
        return to;
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

    @Override
    public String toString() {
        return "Poser depuis les additionels " +
                getColor().forDisplay() +
                ", en (" + to.x + ", " + to.y + ")";
    }
}