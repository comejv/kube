package kube.model.action;

import java.awt.Point;

import kube.model.ModelColor;

public class Build {

    private Point pos;
    private ModelColor c;
    private ModelColor oldColor;

    public Build(ModelColor c, ModelColor oldColor, Point pos) {
        this.c = c;
        this.pos = pos;
        this.oldColor = oldColor;
    }

    public Build(ModelColor c, ModelColor oldColor, int x, int y) {
        this(c, oldColor, new Point(x, y));
    }

    public Point getPos() {
        return pos;
    }

    public ModelColor getModelColor() {
        return c;
    }

    public ModelColor getOldColor() {
        return oldColor;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public void setModelColor(ModelColor c) {
        this.c = c;
    }

    public void setOldColor(ModelColor oldColor) {
        this.oldColor = oldColor;
    }

    @Override
    public String toString() {
        return "Placer sur la montagne " + c + " à la position (" + pos.x + "," + pos.y + "), ce qui remplace " + oldColor;
    }
}
