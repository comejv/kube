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
    public MoveAM(Point to, ModelColor color) {
        super(color);
        this.to =to;
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

    /**
     * Constructor of the class MoveAM from a save string
     * 
     * @param save the string to load
     */
    public MoveAM(String save) {

        String toString, color;
        String[] parts, coords;

        parts = save.split(";");
        color = parts[1];
        toString = parts[2].substring(1, parts[2].length() - 1);

        setColor(ModelColor.getColor(Integer.parseInt(color)));

        coords = toString.split(",");
        this.to = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    /**********
     * SETTERS
     **********/

    public void setTo(Point to) {
        this.to = to;
    }

    public void setTo(int toX, int toY) {
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