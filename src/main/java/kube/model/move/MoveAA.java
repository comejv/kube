package kube.model.move;

import kube.model.Color;

public class MoveAA extends Move {

    Color color;

    // Constructors
    public MoveAA(Color c) {
        super(c);
    }

    // Setters
    public void setColor(Color color) {
        this.color = color;
    }

    // Getters
    public Color getColor() {
        return color;
    }

    // Methods
    public boolean isFromAdditionals() {
        return true;
    }

    public boolean isToAdditionals() {
        return true;
    }

    public boolean isWhite() {
        return color == Color.WHITE;
    }

    @Override
    public String toString() {
        return "MoveAA{" +
                "color=" + color +
                '}';
    }
}
