package kube.model;

import java.util.ArrayList;
import java.awt.Point;

public class Mountain {
    private Color[][] m;
    private int baseSize;

    public Mountain(int size) {
        baseSize = size;
        m = new Color[baseSize][baseSize];
        clear();
    }

    // Getter
    public int getBaseSize() {
        return baseSize;
    }

    public Color[][] getMountain() {
        return m;
    }

    // Setter
    public void setBaseSize(int size) {
        baseSize = size;
    }

    public void setMountain(Color[][] mountain) {
        m = mountain;
    }

    // Method to get the color at the position x, y
    // where getCase(0, 0) return the value of the most left summit case
    public Color getCase(int x, int y) {
        return m[x][y];
    }

    public void setCase(int x, int y, Color c) {
        m[x][y] = c;
    }

    public void remove(int x, int y) {
        setCase(x, y, Color.EMPTY);
    }

    public ArrayList<Point> removable() {
        ArrayList<Point> r = new ArrayList<>();
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < i + 1; j++) {
                if ((i == 0 && j == 0) || getCase(i, j) != Color.EMPTY
                        && (getCase(i - 1, j) == Color.EMPTY && (getCase(i - 1, j + 1) == Color.EMPTY))) {
                    r.add(new Point(i, j));
                }
            }
        }
        return r;
    }

    public ArrayList<Point> compatible(Color c) {
        ArrayList<Point> comp = new ArrayList<>();
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (i == baseSize - 1) {
                    comp.add(new Point(i, j));
                } else if (getCase(i, j) == Color.EMPTY
                        && (getCase(i + 1, j) != Color.EMPTY && (getCase(i + 1, j + 1) != Color.EMPTY))) {
                    if (c == Color.NATURAL || getCase(i + 1, j) == c || (getCase(i + 1, j) == Color.NATURAL)) {
                        if (c == Color.NATURAL || getCase(i + 1, j + 1) == c
                                || (getCase(i + 1, j + 1) == Color.NATURAL)) {
                            comp.add(new Point(i, j));
                        }
                    }
                }
            }
        }
        return comp;
    }

    public int getSize() {
        return baseSize;
    }

    public void clear() {
        m = new Color[baseSize][baseSize];
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < baseSize; j++) {
                m[i][j] = Color.EMPTY;
            }
        }
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

    public String forSave() {
        String s = "";
        return s;
    }
}
