package kube.model.action;

// Import java class
import java.awt.Point;

public class Swap {

    /**********
     * ATTRIBUTES
     **********/

    private Point pos1, pos2;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of Swap class
     * 
     * @param pos1 the first position
     * @param pos2 the second position
     */
    public Swap(Point pos1, Point pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    /**
     * Constructor of Swap class
     * 
     * @param x1 the x coordinate of the first position
     * @param y1 the y coordinate of the first position
     * @param x2 the x coordinate of the second position
     * @param y2 the y coordinate of the second position
     */
    public Swap(int x1, int y1, int x2, int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    /**********
     * SETTERS
     **********/

    public final void setPos1(Point pos1) {
        this.pos1 = pos1;
    }

    public final void setPos2(Point pos2) {
        this.pos2 = pos2;
    }

    /**********
     * GETTERS
     **********/

    public Point getPos1() {
        return pos1;
    }

    public Point getPos2() {
        return pos2;
    }

    /**********
     * METHOD
     **********/

    @Override
    public String toString() {
        return "Echange de la position (" + pos1.x + "," + pos1.y + ") avec la position (" + pos2.x + "," + pos2.y
                + ")";
    }
}
