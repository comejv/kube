package kube.model;

import java.awt.Point;

public class MoveW extends Move {
    
    Point from;

    //Constructors
    public MoveW(Point from) {
        super(Color.WHITE);
        setFrom(from);
    }

    public MoveW(int x, int y) {
        this(new Point(x, y));
    }
    
    //Setter

    public void setFrom(Point from) {
        this.from = from;
    }

    //Getter
    public Point getFrom() {
        return from;
    }

    //Methods
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