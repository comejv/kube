
package kube.model.move;

import java.awt.Point;

import kube.model.Color;

public class MoveMA extends Move {

    private Point from;

    //Constructors
    public MoveMA(Point from, Color color) {
        super(color);
        setFrom(from);
    }

    public MoveMA(int fromX, int fromY, Color color) {
        this(new Point(fromX, fromY), color);
    }

    public boolean isToAdditionals() {
        return true;
    }
    
    //Setters
    public void setFrom(Point from) {
        this.from = from;
    }

    //Getters
    public Point getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "MoveMA{" +
                "from=" + getFrom() +
                ", to=" + getTo() +
                ", color=" + getColor() +
                '}';
    }

}