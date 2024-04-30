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

    Color(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public static Color getColor(int colorCode) {
        for (Color c : Color.values()) {
            if (c.getColorCode() == colorCode)
                return c;
        }
        return NOT_DEFINE;
    }
}
