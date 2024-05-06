package kube.model.move;

import java.awt.Point;

import kube.model.Color;

public class MoveAM extends Move {

    Color color;
    Point to;

    //Constructors
    public MoveAM(Point to, Color color) {
        super();
        setColor(color);
        setTo(to);
    }

    public MoveAM(int x, int y, Color color) {
        this(new Point(x, y), color);
    }

    //Setters
    public void setColor(Color color) {
        this.color = color;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    //Getters
    public Color getColor() {
        return color;
    }

    public Point getTo() {
        return to;
    }

    //Methods
    public boolean isFromAdditionals() {
        return true;
    }

    @Override
    public String toString() {
        return "MoveAM{" +
                "color=" + color +
                ", to=" + to +
                '}';
    }

}