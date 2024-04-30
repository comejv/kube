package kube;

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

    Color(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public Color getColor(int colorCode) {
        switch (colorCode) {
            case 0:
                return Color.EMPTY;
            case 1:
                return Color.WHITE;
            case 2:
                return Color.NATURAL;
            case 3:
                return Color.RED;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.BLUE;
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.BLACK;
            default:
                return Color.EMPTY;
        }
    }
}
