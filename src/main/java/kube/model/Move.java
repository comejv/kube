package kube.model;

import java.awt.Point;

public class Move {
    
    Player player;
    Point from;
    Point to;
    Color color;

    // Constructor
    public Move(Point from, Color color) {
        setFrom(from);
        setColor(color);
    }

    // Setters
    public void setPlayer(Player p) {
        this.player = p;
    }

    public void setFrom(Point f) {
        this.from = f;
    }

    public void setTo(Point t) {
        this.to = t;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    // Getters
    public Player getPlayer() {
        return this.player;
    }

    public Point getFrom() {
        return this.from;
    }

    public Point getTo() {
        return this.to;
    }

    public Color getColor() {
        return this.color;
    }

    // Methods
    public String forSave() {
        String s = "{";
        s += getPlayer().getId() + " ";
        s += "(" + getFrom().x + "," + getFrom().y + ") ";
        s += "(" + getTo().x + "," + getTo().y + ") ";
        s += getColor().toString() + "}";
        return s;
    }
}
