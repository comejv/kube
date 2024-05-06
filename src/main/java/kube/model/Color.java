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


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_DARK_GRAY = "\u001B[90m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";

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
            if (c.getColorCode() == colorCode)
                return c;
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
                return ANSI_WHITE+"▣"+ANSI_RESET;
            case NATURAL:
                return ANSI_DARK_GRAY+"▣"+ANSI_RESET;
            case RED:
                return ANSI_RED+"▣"+ANSI_RESET;
            case GREEN:
                return ANSI_GREEN + "▣" + ANSI_RESET;
            case BLUE:
                return ANSI_BLUE + "▣" + ANSI_RESET;
            case YELLOW:
                return ANSI_YELLOW + "▣" + ANSI_RESET;
            case BLACK:
                return ANSI_PURPLE + "▣" + ANSI_RESET;
            default:
                return " ";
        }
    }

}
