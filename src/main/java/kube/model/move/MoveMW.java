package kube.model.move;

import java.awt.Point;

import kube.model.Color;

public class MoveMW extends Move {

    Point from;

    // Constructors
    public MoveMW(Point from) {
        super(Color.WHITE);
        setFrom(from);
    }

    public MoveMW(int x, int y) {
        this(new Point(x, y));
    }

    // Setter

    public void setFrom(Point from) {
        this.from = from;
    }

    // Getter
    public Point getFrom() {
        return from;
    }

    // Methods
    public boolean isWhite() {
        return true;
    }

    @Override
    public String toString() {
        return "MoveW{" +
                "from=" + from +
                '}';
    }
}
