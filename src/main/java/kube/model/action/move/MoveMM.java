
package kube.model.action.move;

// Import model class
import kube.model.ModelColor;

// Import java class
import java.awt.Point;

public class MoveMM extends Move {

    /**********
     * ATTRIBUTES
     **********/

    private Point from;
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
    public MoveMM(Point from, Point to, ModelColor color) {
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

    public final void setFrom(Point from) {
        this.from = from;
    }

    public final void setFrom(int x, int y) {
        setFrom(new Point(x, y));
    }

    public final void setTo(Point to) {
        this.to = to;
    }

    public final void setTo(int x, int y) {
        setTo(new Point(x, y));
    }

    /**********
     * GETTERS
     **********/

    public Point getFrom() {
        return from;
    }

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

    @Override
    public String toHTML() {
        String s = getPlayer().getName() + " pose " +
                getColor().forDisplayHTML() +
                " depuis sa " + (6 - getFrom().x);
        if (getFrom().x == 5) {
            s += "ere";
        } else {
            s += "eme";
        }
        s += " colonne de sa " + (getFrom().y + 1);
        if (getFrom().y == 0) {
            s += "ere";
        } else {
            s += "eme";
        }
        s += " ligne vers la " + (9 - getTo().x);
        if (getTo().x == 8) {
            s += "ere";
        } else {
            s += "eme";
        }
        s += " colonne de la " + (getTo().y + 1);
        if (getTo().y == 0) {
            s += "ere";
        } else {
            s += "eme";
        }
        s += " ligne";
        return s;
    }
}
