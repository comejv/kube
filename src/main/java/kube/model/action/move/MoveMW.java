package kube.model.action.move;

// Import model class
import kube.model.ModelColor;

// Import java class
import java.awt.Point;

public class MoveMW extends Move {

    /**********
     * ATTRIBUTE
     **********/

    private Point from;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class MoveAM
     * 
     * @param from  the source of the move
     * @param color the color of the moved cube
     */
    public MoveMW(Point from) {
        super(ModelColor.WHITE);
        this.from = from;
    }

    /**
     * Constructor of the class MoveAM
     * 
     * @param fromX the x position of the source of the move
     * @param fromY the y position of the source of the move
     * @param color the color of the moved cube
     */
    public MoveMW(int fromX, int fromY) {
        this(new Point(fromX, fromY));
    }

    /**
     * Constructor of the class MoveMW from a save string
     * 
     * @param save the string to load
     */
    public MoveMW(String save) {

        super(ModelColor.WHITE);

        String fromString;
        String[] parts, coords;

        parts = save.split(";");
        fromString = parts[2].substring(1, parts[2].length() - 1);

        coords = fromString.split(",");
        this.from = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    /**********
     * SETTERS
     **********/

    public final void setFrom(Point from) {
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

    @Override
    public String toHTML() {
        return "Passer son tour " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }
}
