
package kube.model.action.move;

// Import model class
import kube.model.ModelColor;

// Import java class
import java.awt.Point;

public class MoveMA extends Move {

    /**********
     * ATTRIBUTE
     **********/

    private Point from;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class MoveMA
     * 
     * @param from  the source of the move
     * @param color the color of the moved cube
     */
    public MoveMA(Point from, ModelColor color) {
        super(color);
        this.from = from;
    }

    /**
     * Constructor of the class MoveMA
     * 
     * @param fromX the x position of the source of the move
     * @param fromY the y position of the source of the move
     * @param color the color of the moved cube
     */
    public MoveMA(int fromX, int fromY, ModelColor color) {
        this(new Point(fromX, fromY), color);
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

    /**********
     * GETTER
     **********/

    public Point getFrom() {
        return from;
    }

    /**********
     * METHODS
     **********/

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
        return "Recuperer dans ses additionels " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }

    @Override
    public String toHTML() {
        return "Recuperer dans ses additionels " +
                getColor().forDisplayHTML() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }
}
