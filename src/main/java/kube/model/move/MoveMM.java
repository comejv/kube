
package kube.model.move;

import java.awt.Point;

import kube.model.Color;

public class MoveMM extends Move {

    private Point from;
    private Point to;

    // Constructors
    public MoveMM(Point from, Point to, Color color) {
        super(color);
        setFrom(from);
        setTo(to);
    }

    public MoveMM(int fromX, int fromY, int toX, int toY, Color color) {
        this(new Point(fromX, fromY), new Point(toX, toY), color);
    }

    // Setters
    public void setFrom(Point from) {
        this.from = from;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    // Getters
    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

    // Methods
    public boolean isClassicMove() {
        return true;
    }

    @Override
    public String toString() {
        return "MoveMM{" +
                "from=" + getFrom() +
                ", to=" + getTo() +
                ", color=" + getColor() +
                '}';
    }

}
