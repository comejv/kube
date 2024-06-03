package kube.model;

// Import java classes
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Mountain implements Serializable {

    /**********
     * ATTRIBUTES
     **********/

    private ModelColor[][] content;
    private int baseSize;

    /**********
     * CONSTUCTOR
     **********/

    /**
     * Constructor of the class Mountain
     * 
     * @param size the size of the mountain's base
     */
    public Mountain(int size) {
        this.baseSize = size;
        this.content = new ModelColor[size][size];
        clear();
    }

    /**********
     * SETTERS
     **********/

    public final void setMountain(ModelColor[][] mountain) {
        content = mountain;
    }

    public final void setCase(Point p, ModelColor c) {
        setCase(p.x, p.y, c);
    }

    public final void setCase(int x, int y, ModelColor c) {
        content[x][y] = c;
    }

    /**********
     * GETTERS
     **********/

    public int getBaseSize() {
        return baseSize;
    }

    public ModelColor[][] getMountain() {
        return content;
    }

    public ModelColor getCase(Point p) {
        return getCase(p.x, p.y);
    }

    public ModelColor getCase(int x, int y) {
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
        setCase(x, y, ModelColor.EMPTY);
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

        // Loop through the mountain to add removable positions
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                isEmpty = getCase(i, j) == ModelColor.EMPTY;
                isAtPeak = i == 0;
                isTopLeftEmpty = j == 0 || getCase(i - 1, j - 1) == ModelColor.EMPTY;
                isTopRightEmpty = j == i || getCase(i - 1, j) == ModelColor.EMPTY;
                if (!isEmpty && (isAtPeak || (isTopLeftEmpty && isTopRightEmpty))) {
                    r.add(new Point(i, j));
                }
            }
        }

        return r;

    }

    /**
     * Check if the given color is compatible with the given position
     * 
     * @param color the color to check compatibility
     * @return true if the color is compatible, false otherwise
     */
    public boolean isCompatible(int i, int j, ModelColor color) {

        ModelColor natural, empty, bottomLeft, bottomRight;
        boolean isBottomLeftEmpty, isBottomRightEmpty, isBottomEmpty, isNatural, isBottomLeftCompatible,
                isBottomRightCompatible, isCompatible;

        empty = ModelColor.EMPTY;
        natural = ModelColor.NATURAL;
        if (i == getBaseSize() - 1 && getCase(i, j) == empty) {
            return true;
        } else if (getCase(i, j) == empty) {

            bottomLeft = getCase(i + 1, j);
            bottomRight = getCase(i + 1, j + 1);

            isBottomLeftEmpty = bottomLeft == empty;
            isBottomRightEmpty = bottomRight == empty;
            isBottomEmpty = isBottomLeftEmpty || isBottomRightEmpty;

            isNatural = color == natural;
            isBottomLeftCompatible = bottomLeft == color || bottomLeft == natural;
            isBottomRightCompatible = bottomRight == color || bottomRight == natural;
            isCompatible = isNatural || isBottomLeftCompatible || isBottomRightCompatible;

            if (!isBottomEmpty && isCompatible) {
                return true;
            }
        }
        return false;
    }

    /**
     * Give the list of compatible positions corresponding to the given color
     * 
     * @param color the color to check compatibility
     * @return the list of compatible positions
     */
    public ArrayList<Point> compatible(ModelColor color) {

        ArrayList<Point> compatible;
        compatible = new ArrayList<>();

        // Loop through the mountain to add compatible positions
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (isCompatible(i, j, color)) {
                    compatible.add(new Point(i, j));
                }
            }
        }

        return compatible;
    }

    /**
     * Return if the given position creates a penality on the mountain
     * 
     * @param position the position to check
     * @return true if the position creates a penality, false otherwise
     */
    public boolean isPenality(Point position) {
        return isPenality(position.x, position.y);
    }

    /**
     * Return if the given position creates a penality on the mountain
     * 
     * @param x the x position to check
     * @param y the y position to check
     * @return true if the position creates a penality, false otherwise
     */
    public boolean isPenality(int x, int y) {
        return getCase(x + 1, y) == getCase(x + 1, y + 1);
    }

    /**
     * Clear the mountain by setting all cases to empty
     * 
     * @return void
     */
    public void clear() {
        content = new ModelColor[getBaseSize()][getBaseSize()];
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < getBaseSize(); j++) {
                content[i][j] = ModelColor.EMPTY;
            }
        }
    }

    /**
     * Return if the mountain is full
     * 
     * @return true if the mountain is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) == ModelColor.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return if the mountain is empty
     * 
     * @return true if the mountain is empty, false otherwise
     */
    public boolean isEmpty() {
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                if (getCase(i, j) != ModelColor.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Give a string representation of the mountain (for display)
     * 
     * @return the string representation of the mountain
     */
    @Override
    public String toString() {

        String s;
        boolean space;

        s = "";
        space = false;

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

    @Override
    public boolean equals(Object o) {

        Mountain m;

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        m = (Mountain) o;

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

        Mountain copy;

        copy = new Mountain(getBaseSize());
        for (int i = 0; i < getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                copy.setCase(i, j, getCase(i, j));
            }
        }

        return copy;
    }
}
