package kube.model;

import java.awt.Point;

public abstract class Move {

    Player player;
    Color color;

    // Constructors
    public Move() {
        setColor(Color.EMPTY);
    }

    public Move(Color color) {
        setColor(color);
    }

    // Setters
    public void setPlayer(Player p) {
        this.player = p;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    // Getters
    public Player getPlayer() {
        return this.player;
    }

    public Color getColor() {
        return this.color;
    }

    public Point getFrom() {
        return null;
    }

    public Point getTo() {
        return null;
    }

    // Methods
    public boolean isFromAdditionals() {
        return false;
    }

    public boolean isWhite() {
        return false;
    }

    public boolean isClassicMove() {
        return false;
    }

    public String forSave() {
        String s = "{";
        s += getPlayer().getId() + " ";
        s += "(" + getFrom().x + "," + getFrom().y + ") ";
        s += "(" + getTo().x + "," + getTo().y + ") ";
        s += getColor().toString() + "}";
        return s;
    }
}
