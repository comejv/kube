package kube.model.action;

// Import model class
import kube.model.ModelColor;

// Import java class
import java.awt.Point;

public class Build {

    /**********
     * ATTRIBUTES
     **********/

    private Point position;
    private ModelColor color;
    private ModelColor oldColor;

    /**********
     * CONSTRCUTORS
     **********/

    /**
     * Constructor of the class Build
     * 
     * @param color    the color of cube to place
     * @param oldColor the old color in the position
     * @param pos      the position of the cube
     */
    public Build(ModelColor color, ModelColor oldColor, Point pos) {
        this.position = pos;
        this.color = color;
        this.oldColor = oldColor;
    }

    /**
     * Constructor of the class Build
     * 
     * @param color    the color of cube to place
     * @param oldColor the old color in the position
     * @param x        the x position of the cube
     * @param y        the y position of the cube
     */
    public Build(ModelColor color, ModelColor oldColor, int x, int y) {
        this(color, oldColor, new Point(x, y));
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

    public ModelColor getOldColor() {
        return oldColor;
    }

    /**********
     * METHOD
     **********/

    @Override
    public String toString() {
        return "Placer sur la montagne " + color + " à la position (" + position.x + "," + position.y
                + "), ce qui remplace " + oldColor;
    }
}
