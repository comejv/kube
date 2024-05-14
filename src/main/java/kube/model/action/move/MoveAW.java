package kube.model.action.move;

import kube.model.Color;

public class MoveAW extends Move {

    Color color;

    // Constructors
    public MoveAW() {
        super(Color.WHITE);
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

    public boolean isWhite() {
        return true;
    }

    @Override
    public String toString() {
        return "Passer son tour depuis les additionels{" +
                color.forDisplay() +
                '}';
    }
}
