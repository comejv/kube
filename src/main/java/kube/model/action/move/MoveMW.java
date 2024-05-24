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

public class MoveMW extends Move {

    /**********
     * ATTRIBUTE
     **********/

    @JsonProperty("from")
    private Point from;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class MoveAM
     * 
     * @param from  the source of the move
     * @param color the color of the moved cube
     */
    @JsonCreator
    public MoveMW(@JsonProperty("from") Point from) {
        super(ModelColor.WHITE);
        this.from = from;
    }

    /**
     * Constructor of the class MoveAM
     * 
     * @param fromX the x position of the source of the move
     * @param fromY the y position of the source of the move
     * @param color the color of the moved cube
     */
    public MoveMW(int fromX, int fromY) {
        this(new Point(fromX, fromY));
    }

    /**********
     * SETTERS
     **********/

    @JsonSetter("from")
    public final void setFrom(Point from) {
        this.from = from;
    }

    public final void setFrom(int x, int y) {
        setFrom(new Point(x, y));
    }

    /**********
     * GETTER
     **********/

    @JsonGetter("from")
    public Point getFrom() {
        return from;
    }

    /**********
     * METHODS
     **********/

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
        return "Passer son tour " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }
}
