package kube.model;

import java.util.ArrayList;
import java.awt.Point;

public class Mountain {
    private int[] m;
    private int baseSize;
    private static final int nbBitPerColor = 7;

    public Mountain(int size) {
        baseSize = size;
        m = new int[baseSize];
    }

    // Method to get the color at the position x, y
    // where getCase(0, 0) return the value of the most left summit case
    public Color getCase(int x, int y) {
        return Color.getColor((m[x] >> ((baseSize - y) * nbBitPerColor)) & 127);
    }

    public void setCase(int x, int y, Color c) {
        m[x] = m[x] & (~127 << ((baseSize - y) * nbBitPerColor));
        m[x] = m[x] & (c.getColorCode() << ((baseSize - y) * nbBitPerColor));
    }

    public void remove(int x, int y) {
        setCase(x, y, Color.EMPTY);
    }

    public ArrayList<Point> removable() {
        ArrayList<Point> r = new ArrayList<>();
        for (int i = 0; i < baseSize; i++){
            for (int j = 0; j < i+1; j++){
                if (i == 0 || (getCase(i-1, j) == Color.EMPTY && (getCase(i-1, j+1) == Color.EMPTY)) && getCase(i, j) != Color.EMPTY){
                    r.add(new Point(i, j));
                }
            }
        }
        return r;
    }

    public ArrayList<Point> compatible() {
        ArrayList<Point> c = new ArrayList<>();
        for (int i = 0; i < baseSize; i++){
            for (int j = 0; j < i+1; j++){
                if (i == (baseSize - 1) || (getCase(i+1, j) != Color.EMPTY && (getCase(i+1, j+1) != Color.EMPTY)) && getCase(i, j) == Color.EMPTY){
                    c.add(new Point(i, j));
                }
            }
        }
        return c;
    }

    public int getSize() {
        return baseSize;
    }

    public void clear() {
        m = new int[baseSize];
    }

    public Boolean isFull() {
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) == Color.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean isEmpty() {
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) != Color.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}
