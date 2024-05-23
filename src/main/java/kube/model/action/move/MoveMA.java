
package kube.model.action.move;

import java.awt.Point;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import kube.model.ModelColor;

public class MoveMA extends Move {

    /**********
     * ATTRIBUTE
     **********/

    @JsonProperty("from")
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
    @JsonCreator
    public MoveMA(@JsonProperty("from") Point from, @JsonProperty("color") ModelColor color) {
        super(color);
        this.from = from;
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

    /**********
     * SETTERS
     **********/

    @JsonSetter("from")
    public void setFrom(Point from) {
        this.from = from;
    }

    public void setFrom(int x, int y) {
        setFrom(new Point(x, y));
    }

    /**********
     * GETTER
     **********/

    @JsonGetter("from")
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

    @Override
    public String toString() {
        return "Recuperer dans ses additionels " +
                getColor().forDisplay() +
                " depuis (" + getFrom().x + ", " + getFrom().y + ")";
    }

}
