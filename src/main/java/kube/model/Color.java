package kube.model;

public enum Color {
    NOT_DEFINE(-1),
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
        setColorCode(colorCode);;
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
        switch (this) {
            case EMPTY:
                return "EMPTY";
            case WHITE:
                return "WHITE";
            case NATURAL:
                return "NATURAL";
            case RED:
                return "RED";
            case GREEN:
                return "GREEN";
            case BLUE:
                return "BLUE";
            case YELLOW:
                return "YELLOW";
            case BLACK:
                return "BALCK";
            default:
                return "NOT_DEFINE";
        }
    }

    // Methods
    public static Color getColor(int colorCode) {
        for (Color c : Color.values()) {
            if (c.getColorCode() == colorCode)
                return c;
        }
        return NOT_DEFINE;
    }

    public static Color[] getAllColored() { 
        return new Color[]{RED, GREEN, BLUE, YELLOW, BLACK};
    }
}
