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

    // public Move(Point from, Point to, Color color) {
    // setFrom(from);
    // setTo(to);
    // setColor(color);
    // }

    // public Move(int x1, int x2, int y1, int y2, Color color) {
    // this(new Point(x1, y1), new Point(x2, y2), color);
    // }

    // public Move(boolean fromAdditional, Color color) {
    // fromAdditional = true;
    // setColor(color);
    // }

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
