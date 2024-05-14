package kube.model;

import java.util.ArrayList;
import java.util.Objects;
import java.awt.Point;

public class Mountain {

    /**********
     * ATTRIBUTES
     **********/
    private Color[][] content;
    private int baseSize;

    /**********
     * CONSTUCTOR
     **********/
    public Mountain(int size) {
        setBaseSize(size);
        setMountain(new Color[getBaseSize()][getBaseSize()]);
        clear();
    }

    /**********
     * SETTERS
     **********/
    public void setBaseSize(int size) {
        baseSize = size;
    }

    public void setMountain(Color[][] mountain) {
        content = mountain;
    }

    public void setCase(Point p, Color c) {
        setCase(p.x, p.y, c);
    }

    public void setCase(int x, int y, Color c) {
        content[x][y] = c;
    }

    /**********
     * GETTERS
     **********/
    public int getBaseSize() {
        return baseSize;
    }

    public Color[][] getMountain() {
        return content;
    }

    public Color getCase(Point p) {
        return getCase(p.x, p.y);
    }

    public Color getCase(int x, int y) {
        return content[x][y];
    }

    /**********
     * METHODS
     **********/

    /**
     * Remove a case from the mountain
     * 
     * @param p the position to remove
     * @return void
     */
    public void remove(Point p) {
        remove(p.x, p.y);
    }

    /**
     * Remove a case from the mountain
     * 
     * @param x the x position to remove
     * @param y the y position to remove
     * @return void
     */
    public void remove(int x, int y) {
        setCase(x, y, Color.EMPTY);
    }

    /**
     * Give the list of removable positions
     * 
     * @return the list of removable positions
     */
    public ArrayList<Point> removable() {

        ArrayList<Point> r;
        boolean isEmpty, isAtPeak, isTopLeftEmpty, isTopRightEmpty;

        r = new ArrayList<>();

        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                isEmpty = getCase(i, j) == Color.EMPTY;
                isAtPeak = i == 0;
                isTopLeftEmpty = j == 0 || getCase(i - 1, j - 1) == Color.EMPTY;
                isTopRightEmpty = j == i || getCase(i - 1, j) == Color.EMPTY;
                if (!isEmpty && (isAtPeak || (isTopLeftEmpty && isTopRightEmpty))) {
                    r.add(new Point(i, j));
                }
            }
        }
        return r;
    }

    public ArrayList<Point> compatible(Color c) {

        ArrayList<Point> comp;
        Color natural, empty, bottomLeft, bottomRight;
        boolean isBottomLeftEmpty, isBottomRightEmpty, isBottomEmpty, isNatural, isBottomLeftCompatible,
                isBottomRightCompatible, isCompatible;
        comp = new ArrayList<>();

        empty = Color.EMPTY;
        natural = Color.NATURAL;

        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (i == getBaseSize() - 1 && getCase(i, j) == empty) {
                    comp.add(new Point(i, j));
                } else if (getCase(i, j) == empty) {

                    bottomLeft = getCase(i + 1, j);
                    bottomRight = getCase(i + 1, j + 1);

                    isBottomLeftEmpty = bottomLeft == empty;
                    isBottomRightEmpty = bottomRight == empty;
                    isBottomEmpty = isBottomLeftEmpty || isBottomRightEmpty;

                    isNatural = c == natural;
                    isBottomLeftCompatible = bottomLeft == c || bottomLeft == natural;
                    isBottomRightCompatible = bottomRight == c || bottomRight == natural;
                    isCompatible = isNatural || isBottomLeftCompatible || isBottomRightCompatible;

                    if (!isBottomEmpty && isCompatible) {
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
        content = new Color[getBaseSize()][getBaseSize()];
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < getBaseSize(); j++) {
                content[i][j] = Color.EMPTY;
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
        if (getBaseSize() != m.getBaseSize()) {
            return false;
        }

        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) != m.getCase(i, j)) {
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
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                copy.setCase(i, j, getCase(i, j));
            }
        }
        return copy;
    }
}
