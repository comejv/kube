package kube.model.action.move;

import java.awt.Point;

import kube.model.action.move.MoveMW;
import kube.model.ModelColor;
import kube.model.Player;

public abstract class Move {

    /**********
     * ATTRIBUTES
     **********/

    private Player player;
    private ModelColor color;

    /**********
     * CONSTRUCTORS
     **********/

    public Move() {
        setColor(ModelColor.EMPTY);
    }

    public Move(ModelColor color) {
        setColor(color);
    }

    /**********
     * SETTERS
     **********/

    public void setPlayer(Player p) {
        this.player = p;
    }

    public void setColor(ModelColor c) {
        this.color = c;
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
        // TODO: check if it is needed to add the player id
        // or if we can get it from the first player of the game
        return getPlayer().getId() + ";" + getColor().forSave();
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

    static public Move fromSave(String save) {

        String moveType, color, player, from, to;
        String [] parts, pointCoordinates;

        save = save.substring(1, save.length() - 1);
        parts = save.split(";");

        moveType = parts[0];
        player = parts[1];
        color = parts[2];

        switch (moveType) {
            case "MW":
                from = parts[3].substring(1, parts[2].length() - 1);
                pointCoordinates = from.split(",");
                return new MoveMW(Integer.parseInt(pointCoordinates[0]), Integer.parseInt(pointCoordinates[1]));
        }

        return null;
    }
}
