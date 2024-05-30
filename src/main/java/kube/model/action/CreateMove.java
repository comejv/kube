package kube.model.action;

import java.awt.Point;

import kube.model.ModelColor;
import kube.model.Player;

public class CreateMove {

    Point posFrom;
    Player playerFrom;
    Point posTo;
    Player playerTo;
    ModelColor color;

    public CreateMove(Point posFrom, Player playerFrom, Point posTo, Player playerTo, ModelColor color) {
        this.posFrom = posFrom;
        this.playerFrom = playerFrom;
        this.posTo = posTo;
        this.playerTo = playerTo;
        this.color = color;
    }

    public Point getPosFrom() {
        return posFrom;
    }

    public Point getPosTo() {
        return posTo;
    }

    public Player getPlayerFrom() {
        return playerFrom;
    }

    public Player getPlayerTo() {
        return playerTo;
    }

    public ModelColor getModelColor() {
        return color;
    }


    @Override
    public String toString() {
        return "Un coup déconstruit de " + getPlayerFrom() + ":" + getPosFrom() + " à " + getPlayerTo() + ":"
                + getPosTo();
    }
}
