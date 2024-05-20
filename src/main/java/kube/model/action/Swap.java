package kube.model.action;

import java.awt.Point;

public class Swap {

    private Point pos1;
    private Point pos2;

    public Swap(Point pos1, Point pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Swap(int x1, int y1, int x2, int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    public Point getPos1() {
        return pos1;
    }

    public Point getPos2() {
        return pos2;
    }

    public void setPos1(Point pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Point pos2) {
        this.pos2 = pos2;
    }

    @Override
    public String toString() {
        return "Echange de la position (" + pos1.x + "," + pos1.y + ") avec la position (" + pos2.x + "," + pos2.y + ")";
    }
}
