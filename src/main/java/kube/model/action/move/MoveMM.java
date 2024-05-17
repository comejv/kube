
package kube.model.action.move;

import java.awt.Point;

import kube.model.ModelColor;

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
        setFrom(from);
        setTo(to);
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

    /**
     * Constructor of the class MoveMM from a save string
     * 
     * @param save the string to load
     */
    public MoveMM(String save) {

        String from, to, color;
        String[] parts, coords;

        parts = save.split(";");
        color = parts[1];
        from = parts[2].substring(1, parts[2].length() - 1);
        to = parts[3].substring(1, parts[3].length() - 1);

        setColor(ModelColor.getColor(Integer.parseInt(color)));

        coords = from.split(",");
        setFrom(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

        coords = to.split(",");
        setTo(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    /**********
     * SETTERS
     **********/

    public void setFrom(Point from) {
        this.from = from;
    }

    public void setFrom(int x, int y) {
        setFrom(new Point(x, y));
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public void setTo(int x, int y) {
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

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    @Override
    public String forSave() {
        return "{MM;" + super.forSave() + ";" +
                "(" + getFrom().x + "," + getFrom().y + ");" +
                "(" + getTo().x + "," + getTo().y + ")}";
    }

    @Override
    public String toString() {
        return "Poser " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")" +
                " en (" + getTo().x + ", " + getTo().y + ")";
    }
}