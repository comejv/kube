package kube.model;

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
            if (c.getColorCode() == colorCode)
                return c;
        }
        return EMPTY;
    }

    public static Color[] getAllColored() {
        return new Color[] { RED, GREEN, BLUE, YELLOW, BLACK };
    }
    
    public static Comparator<Color> compareByValue = new Comparator<Color>() {
        @Override
        public int compare(Color c1, Color c2) {
            return c1.getColorCode() - c2.getColorCode();
        }
    };

}
