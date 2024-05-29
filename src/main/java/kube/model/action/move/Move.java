package kube.model.action.move;

// Import model classes
import kube.model.ModelColor;
import kube.model.Player;

// Import java classes
import java.awt.Point;
import java.io.Serializable;

public abstract class Move implements Serializable {

    /**********
     * ATTRIBUTES
     **********/

    private Player player;
    private ModelColor color;

    /**********
     * CONSTRUCTORS
     **********/

    public Move() {
        this.color = ModelColor.EMPTY;
    }

    /**
     * Constructor of the class Move
     * 
     * @param color
     */
    public Move(ModelColor color) {
        this.color = color;
    }

    /**********
     * SETTERS
     **********/

    public void setPlayer(Player player) {
        this.player = player;
    }

    public final void setColor(ModelColor color) {
        this.color = color;
    }

    /**********
     * GETTERS
     **********/

    public Player getPlayer() {
        return this.player;
    }

    public ModelColor getColor() {
        return this.color;
    }

    public Point getFrom() {
        return null;
    }

    public Point getTo() {
        return null;
    }

    /**********
     * METHODS
     **********/

    /**
     * Check if the move is from the additionals
     *
     * @return true if the move is from the additionals, false otherwise
     */
    public boolean isFromAdditionals() {
        return false;
    }

    /**
     * Check if the move is to the additionals (penality)
     * 
     * @return true if the move is to the additionals, false otherwise
     */
    public boolean isToAdditionals() {
        return false;
    }

    /**
     * Check if the move is a white move
     * 
     * @return true if the move is a white move, false otherwise
     */
    public boolean isWhite() {
        return false;
    }

    /**
     * Check if the move is a classic move
     * 
     * @return true if the move is a classic move, false otherwise
     */
    public boolean isClassicMove() {
        return false;
    }

    /**
     * Give a string representation of the move for saving
     * 
     * @return a string representation of the move for saving
     */
    public String forSave() {
        return getColor().forSave();
    }

    public String toHTML() {
        return "";
    }

    @Override
    public boolean equals(Object o) {

        Move that;

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        that = (Move) o;
        if (!this.getClass().equals(that.getClass()))
            return false;

        if (getFrom() != null ? !getFrom().equals(that.getFrom()) : that.getFrom() != null)
            return false;

        if (getTo() != null ? !getTo().equals(that.getTo()) : that.getTo() != null)
            return false;

        return getColor() == that.getColor();
    }

    public static Move fromSave(String save) throws IllegalArgumentException {

        String moveType;
        String[] parts;

        save = save.substring(1, save.length() - 1);
        parts = save.split(";");
        moveType = parts[0];

        switch (moveType) {
            case "MW":
                return new MoveMW(save);
            case "MM":
                return new MoveMM(save);
            case "AM":
                return new MoveAM(save);
            case "AW":
                return new MoveAW();
            case "MA":
                return new MoveMA(save);
            case "AA":
                return new MoveAA(save);
            default:
                throw new IllegalArgumentException("Unknown move type");
        }
    }
}
