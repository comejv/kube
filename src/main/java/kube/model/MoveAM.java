package kube.model;

import java.awt.Point;

public class MoveAM extends Move {

    int pos;
    Point to;

    //Constructors
    public MoveAM(int pos, Point to) {
        super();
        setPos(pos);
        setTo(to);
    }

    public MoveAM(int pos, int x, int y) {
        this(pos, new Point(x, y));
    }

    //Setters
    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    //Getters
    public int getPos() {
        return pos;
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
                "pos=" + pos +
                ", to=" + to +
                '}';
    }

}