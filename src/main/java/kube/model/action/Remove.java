package kube.model.action;

import java.awt.Point;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import kube.model.ModelColor;

public class Remove {

    private Point pos;

    public Remove(Point pos) {
        this.pos = pos;
    }

    public Remove(int x, int y) {
        this(new Point(x, y));
    }

    public Point getPos() {
        return pos;
    }


    public void setPos(Point pos) {
        this.pos = pos;
    }


    @Override
    public String toString() {
        return "Retirer de la montagne à la position (" + pos.x + "," + pos.y + ")";
    }
}
