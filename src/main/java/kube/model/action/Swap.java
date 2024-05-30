package kube.model.action;

// Import java class
import java.awt.Point;

public class Swap {

    /**********
     * ATTRIBUTES
     **********/

    private Point from, to;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class Swap
     * 
     * @param from the first position
     * @param to the second position
     */
    public Swap(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Constructor of the class Swap
     * 
     * @param fromX the x coordinate of the first position
     * @param fromY the y coordinate of the first position
     * @param toX the x coordinate of the second position
     * @param toY the y coordinate of the second position
     */
    public Swap(int fromX, int fromY, int toX, int toY) {
        this(new Point(fromX, fromY), new Point(toX, toY));
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
     * METHOD
     **********/
    
    @Override
    public String toString() {
        return "Echange de la position (" + from.x + "," + from.y + ") avec la position (" + to.x + "," + to.y + ")";
    }
}
