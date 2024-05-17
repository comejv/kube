
package kube.model.action.move;

import java.awt.Point;

import kube.model.ModelColor;

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
        setFrom(from);
    }

    /**
     * Constructor of the class MoveAM
     * 
     * @param fromX the x position of the source of the move
     * @param fromY the y position of the source of the move
     * @param color the color of the moved cube
     */
    public MoveMA(int fromX, int fromY, ModelColor color) {
        this(new Point(fromX, fromY), color);
    }

    /**
     * Constructor of the class MoveMA from a save string
     * 
     * @param save the string to load
     */
    public MoveMA(String save) {

        String color, from;
        String[] parts, coords;

        parts = save.split(";");
        color = parts[1];
        from = parts[2].substring(1, parts[2].length() - 1);

        setColor(ModelColor.getColor(Integer.parseInt(color)));

        coords = from.split(",");
        setFrom(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
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

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    @Override
    public String forSave() {
        return "{MA;" + super.forSave() + ";" +
                "(" + getFrom().x + "," + getFrom().y + ")}";
    }

    @Override
    public String toString() {
        return "Recuperer dans ses additionels " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }

}
