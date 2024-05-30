package kube.model.action.move;

// Import model class
import kube.model.ModelColor;

// Import java class
import java.awt.Point;

public class MoveAM extends Move {

    /**********
     * ATTRIBUTE
     **********/

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
    public MoveAM(Point to, ModelColor color) {
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

    public final void setTo(Point to) {
        this.to = to;
    }

    public final void setTo(int toX, int toY) {
        setTo(new Point(toX, toY));
    }

    /**********
     * GETTER
     **********/

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

    @Override
    public String toHTML() {
        return "Poser depuis les additionels " +
                getColor().forDisplayHTML() +
                ", en (" + to.x + ", " + to.y + ")";
    }
}
