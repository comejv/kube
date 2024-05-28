package kube.model.action;

import java.awt.Point;

import kube.model.ModelColor;

public class Remove {

    private Point pos;
    private ModelColor c;

    public Remove(ModelColor c, Point pos) {
        this.c = c;
        this.pos = pos;
    }

    public Remove(ModelColor c, int x, int y) {
        this(c, new Point(x, y));
    }

    public Point getPos() {
        return pos;
    }

    public ModelColor getModelColor() {
        return c;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public void setModelColor(ModelColor c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Retirer de la montagne " + c + " à la position (" + pos.x + "," + pos.y + ")";
    }
}
