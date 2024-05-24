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

public class MoveMM extends Move {

    /**********
     * ATTRIBUTES
     **********/

    @JsonProperty("from")
    private Point from;

    @JsonProperty("to")
    private Point to;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class MoveAM
     *
     * @param from  the source of the move
     * @param to    the destination of the move
     * @param color the color of the moved cube
     */
    @JsonCreator
    public MoveMM(@JsonProperty("from") Point from, @JsonProperty("to") Point to,
            @JsonProperty("color") ModelColor color) {
        super(color);
        this.from = from;
        this.to = to;
    }

    /**
     * Constructor of the class MoveAM
     * 
     * @param fromX the x position of the source of the move
     * @param fromY the y position of the source of the move
     * @param toX   the x position of the destination of the move
     * @param toY   the y position of the destination of the move
     * @param color the color of the moved cube
     */
    public MoveMM(int fromX, int fromY, int toX, int toY, ModelColor color) {
        this(new Point(fromX, fromY), new Point(toX, toY), color);
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

    @JsonSetter("to")
    public final void setTo(Point to) {
        this.to = to;
    }

    public final void setTo(int x, int y) {
        setTo(new Point(x, y));
    }

    /**********
     * GETTERS
     **********/

    @JsonGetter("from")
    public Point getFrom() {
        return from;
    }

    @JsonGetter("to")
    public Point getTo() {
        return to;
    }

    /**********
     * METHODS
     **********/

    /**
     * Check if the move is a classic move
     * 
     * @return true if the move is a classic move, false otherwise
     */
    @Override
    public boolean isClassicMove() {
        return true;
    }

    @Override
    public String toString() {
        return "Poser " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")" +
                " en (" + getTo().x + ", " + getTo().y + ")";
    }
}