package kube.model.action;

// Import model class
import kube.model.ModelColor;

// Import java class
import java.awt.Point;

public class Remove {

    /**********
     * ATTRIBUTES
     **********/

    private Point position;
    private ModelColor color;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class Remove
     * 
     * @param color    the color of the piece
     * @param position the position of the piece
     */
    public Remove(ModelColor color, Point position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Constructor of the class Remove
     * 
     * @param color the color of the piece
     * @param x     the x position of the piece
     * @param y     the y position of the piece
     */
    public Remove(ModelColor color, int x, int y) {
        this(color, new Point(x, y));
    }

    /**********
     * GETTERS
     **********/

    public Point getPosition() {
        return position;
    }

    public ModelColor getModelColor() {
        return color;
    }

    /**********
     * METHODS
     **********/

    @Override
    public String toString() {
        return "Retirer de la montagne " + color + " à la position (" + position.x + "," + position.y + ")";
    }
}
