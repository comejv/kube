package kube.model.action;

// Import model classes
import kube.model.ModelColor;
import kube.model.Player;

// Import java class
import java.awt.Point;

public class CreateMove {

    /**********
     * ATTRIBUTES
     **********/

    Point from, to;
    Player playerFrom, playerTo;
    ModelColor color;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class CreateMove
     * 
     * @param from       the first position
     * @param playerFrom the player of the first position
     * @param to         the second position
     * @param playerTo   the player of the second position
     * @param color      the color of the piece
     */
    public CreateMove(Point from, Player playerFrom, Point to, Player playerTo, ModelColor color) {
        this.from = from;
        this.playerFrom = playerFrom;
        this.to = to;
        this.playerTo = playerTo;
        this.color = color;
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

    public Player getPlayerFrom() {
        return playerFrom;
    }

    public Player getPlayerTo() {
        return playerTo;
    }

    public ModelColor getModelColor() {
        return color;
    }

    /**********
     * METHOD
     **********/

    @Override
    public String toString() {
        return "Un coup déconstruit de " + getPlayerFrom() + ":" + getFrom() + " à " + getPlayerTo() + ":"
                + getTo();
    }
}
