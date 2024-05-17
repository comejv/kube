package kube.model.action.move;

import java.awt.Point;

import kube.model.ModelColor;

public class MoveMW extends Move {

    /**********
     * ATTRIBUTE
     **********/

    Point from;
    
    /**********
     * CONSTRUCTORS
     **********/

     /**
      * Constructor of the class MoveAM

      * @param from the source of the move
      * @param color the color of the moved cube
      */
    public MoveMW(Point from) {
        super(ModelColor.WHITE);
        setFrom(from);
    }

    /**********
     * CONSTRUCTORS
     **********/

     /**
      * Constructor of the class MoveAM

      * @param fromX the x position of the source of the move
      * @param fromY the y position of the source of the move
      * @param color the color of the moved cube
      */
    public MoveMW(int fromX, int fromY) {
        this(new Point(fromX, fromY));
    }

    /**********
     * SETTER
     **********/

    public void setFrom(Point from) {
        this.from = from;
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
     * Check if the move is a white move
     * 
     * @return true if the move is a white move, false otherwise
     */
    @Override
    public boolean isWhite() {
        return true;
    }

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    @Override
    public String forSave() {
        return "{MW;" + super.forSave() + ";" +
                "(" + getFrom().x + "," + getFrom().y + ")}";
    }

    @Override
    public String toString() {
        return "Passer son tour " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }
}
