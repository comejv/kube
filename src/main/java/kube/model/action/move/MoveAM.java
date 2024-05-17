package kube.model.action.move;

import java.awt.Point;

import kube.model.ModelColor;

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
      * @param to the destination of the move
      * @param color the color of the moved cube
      */
    public MoveAM(Point to, ModelColor c) {
        super(c);
        setTo(to);
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
     * SETTER
     **********/

    public void setTo(Point to) {
        this.to = to;
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

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    @Override
    public String forSave() {
        return "{AM;" + super.forSave() + ";" +
                "(" + to.x + "," + to.y + ")}";
    }

    @Override
    public String toString() {
        return "Poser depuis les additionels " +
                getColor().forDisplay() +
                ", en (" + to.x + ", " + to.y + ")";
    }

}
