package kube.model;

import java.util.Random;

import java.util.Comparator;

public enum Color {

    EMPTY(0),
    WHITE(1),
    NATURAL(2),
    RED(3),
    GREEN(4),
    BLUE(5),
    YELLOW(6),
    BLACK(7);

    int colorCode;

    // Constructor
    Color(int colorCode) {
        setColorCode(colorCode);
    }

    // Setter
    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    // Getter
    public int getColorCode() {
        return this.colorCode;
    }

    // toString()
    public String toString() {
        return this.name();
    }

    // Methods
    public static Color getColor(int colorCode) {

        for (Color c : Color.values()) {

            if (c.getColorCode() == colorCode) {

                return c;
            }
        }
        return EMPTY;
    }

    public static Color[] getAllColored() {
        return new Color[] { RED, GREEN, BLUE, YELLOW, BLACK };
    }

    public static Color getRandomColor() {

        Random r = new Random();
        return Color.values()[r.nextInt(Color.values().length)];
    }

    public static Comparator<Color> compareByValue = new Comparator<Color>() {

        @Override
        public int compare(Color c1, Color c2) {

            return c1.getColorCode() - c2.getColorCode();
        }
    };

    public String forDisplay() {

        switch (this) {

            case EMPTY:
                return " ";
            case WHITE:
                return "\u001B[37m" + "▣" + "\u001B[0m";
            case NATURAL:
                return "\u001B[90m" + "▣" + "\u001B[0m";
            case RED:
                return "\u001B[31m" + "▣" + "\u001B[0m";
            case GREEN:
                return "\u001B[32m" + "▣" + "\u001B[0m";
            case BLUE:
                return "\u001B[34m" + "▣" + "\u001B[0m";
            case YELLOW:
                return "\u001B[33m" + "▣" + "\u001B[0m";
            case BLACK:
                return "\u001B[35m" + "▣" + "\u001B[0m";
            default:
                return " ";
        }
    }
}
