package kube.model;

import java.util.ArrayList;
import java.util.Objects;
import java.awt.Point;

public class Mountain {
    private Color[][] m;
    private int baseSize;

    public Mountain(int size) {
        setBaseSize(size);
        setMountain(new Color[getBaseSize()][getBaseSize()]);
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

    public Color getCase(Point p) {
        return getCase(p.x, p.y);
    }

    public void setCase(int x, int y, Color c) {
        m[x][y] = c;
    }

    public void remove(Point p) {
        remove(p.x, p.y);
    }

    public void remove(int x, int y) {
        setCase(x, y, Color.EMPTY);
    }

    public ArrayList<Point> removable() {
        ArrayList<Point> r = new ArrayList<>();
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) != Color.EMPTY &&
                        (i == 0 || (getCase(i - 1, j) == Color.EMPTY
                                && (j == 0 || getCase(i - 1, j - 1) == Color.EMPTY)))) {
                    r.add(new Point(i, j));
                }
            }
        }
        return r;
    }

    public ArrayList<Point> compatible(Color c) {
        ArrayList<Point> comp = new ArrayList<>();
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (i == getBaseSize() - 1 && getCase(i, j) == Color.EMPTY) {
                    comp.add(new Point(i, j));
                } else if (getCase(i, j) == Color.EMPTY
                        && (getCase(i + 1, j) != Color.EMPTY && (getCase(i + 1, j + 1) != Color.EMPTY))) {
                    if (c == Color.NATURAL || getCase(i + 1, j) == c || (getCase(i + 1, j) == Color.NATURAL) ||
                            getCase(i + 1, j + 1) == c || (getCase(i + 1, j + 1) == Color.NATURAL)) {
                        comp.add(new Point(i, j));
                    }
                }
            }
        }
        return comp;
    }

    public boolean isPenality(Point p) {
        return isPenality(p.x, p.y);
    }

    public boolean isPenality(int x, int y) {
        return getCase(x + 1, y) == getCase(x + 1, y + 1);
    }

    public void clear() {
        m = new Color[getBaseSize()][getBaseSize()];
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < getBaseSize(); j++) {
                m[i][j] = Color.EMPTY;
            }
        }
    }

    public Boolean isFull() {
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) == Color.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean isEmpty() {
        for (int i = 0; i < getBaseSize(); i++) {
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
        // TODO: Implement this method
        return s;
    }

    @Override
    public String toString() {
        String s = "";
        boolean space = false;
        for (int i = 0; i < getBaseSize(); i++) {
            s += i + " ";
            for (int j = 0; j < getBaseSize() - i - 1; j++) {
                s += " ";
            }
            for (int j = 0; j < i + 1; j++) {
                s += getCase(i, j).forDisplay() + " ";
            }
            s += "\n";

            space = !space;
        }
        for (int i = 0; i < getBaseSize(); i++) {
            s += " " + i;
        }

        return s;
    }

    public String forDisplay() {
        String s = "";
        // TODO: Implement this method
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mountain m = (Mountain) o;
        if (getBaseSize() != m.getBaseSize()){
            return false;
        }
        
        for (int i = 0; i < getBaseSize(); i++){
            for (int j = 0; j < i + 1; j++){
                if (getCase(i, j) != m.getCase(i, j)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) getMountain());
    }

    @Override
    public Mountain clone() {
        Mountain copy = new Mountain(getBaseSize());
        for (int i = 0; i < getBaseSize(); i++){
            for (int j = 0; j < i + 1; j++){
                copy.setCase(i, j, getCase(i, j));
            }
        }
        return copy;
    }
}
