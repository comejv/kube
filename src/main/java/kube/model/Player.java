package kube.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.Point;

public class Player {

    int id, whiteUsed;
    String name;
    Mountain mountain;
    ArrayList<Color> additional;
    HashMap<Color, Integer> avalaibleToBuild;
    // Constructor

    public Player(int id) {
        setId(id);
        setWhiteUsed(0);
        setMountain(new Mountain(6));
        clearMountain();
        setAdditional(new ArrayList<Color>());
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setWhiteUsed(int whiteUsed) {
        this.whiteUsed = whiteUsed;
    }

    public void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdditional(ArrayList<Color> additional) {
        this.additional = additional;
    }

    public void setAvalaibleToBuild(HashMap<Color, Integer> avalaibleToBuild) {
        this.avalaibleToBuild = avalaibleToBuild;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public int getWhiteUsed() {
        return this.whiteUsed;
    }

    public Mountain getMountain() {
        return this.mountain;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Color> getAdditional() {
        return this.additional;
    }

    public HashMap<Color, Integer> getAvalaibleToBuild() {
        return this.avalaibleToBuild;
    }

    // Methods
    public void addAdditional(Color color) {
        getAdditional().add(color);
    }

    public Color removeAdditional(int pos) {
        return getAdditional().remove(pos);
    }

    public Boolean addToMountain(Point point, Color color) {
        return addToMountain(point.x, point.y, color);
    }

    public Boolean addToMountain(int l, int c, Color color) {
        if (l < 0 || c < 0 || l < c || l >= getMountain().getBaseSize()) {
            return false;
        }
        int n;
        Color colb = getMountain().getCase(l, c);
        if ((n = getAvalaibleToBuild().get(color)) > 0) {
            getMountain().setCase(l, c, color);
            getAvalaibleToBuild().put(color, n);
            if (colb != Color.EMPTY) {
                getAvalaibleToBuild().put(colb, getAvalaibleToBuild().get(colb) + 1);
            }
            return true;
        }
        return false;
    }

    public Color removeFromMountain(Point point) {
        return removeFromMountain(point.x, point.y);
    }

    public void removeToAvailableToBuild(Point p) {
        removeToAvailableToBuild(p.x, p.y);
    }

    public void removeToAvailableToBuild(int l, int c) {
        Color color;
        if ((color = getMountain().getCase(l, c)) != Color.WHITE){
            getMountain().remove(l, c);
            getAvalaibleToBuild().put(color, getAvalaibleToBuild().get(color) + 1);
        }
    }

    public Color removeFromMountain(int l, int c) {
        Color col = getMountain().getCase(l, c);
        getMountain().remove(l, c);
        return col;
    }

    public void clearMountain() {
        getMountain().clear();
    }

    public boolean isMountainFull() {
        return getMountain().isFull();
    }

    public boolean isMountainEmpty() {
        return getMountain().isEmpty();
    }

    public String forSave() {
        String s = "{";
        s += getId() + "\n {";
        s += getMountain().forSave() + "}";
        s += "{";
        for (Color c : getAdditional()) {
            s += c.toString() + ",";
        }
        if (getAdditional().size() > 0)
            s = s.substring(0, s.length() - 1);
        s += "}";
        return s;
    }

    public String toString() {
        String s = "Player " + getId();
        s += "\nMountain: " + getMountain().toString();
        s += "\nAdditional: ";
        s += "\nMountain: " + getMountain().toString();
        s += "\nAdditional: ";
        for (Color c : getAdditional()) {
            s += c.toString() + " ";
        }
        return s;
    }

    public HashSet<Color> getPlayableColors() {
        HashSet<Color> playable = new HashSet<>();
        HashSet<Color> toTest = new HashSet<>();
        ArrayList<Point> removable = getMountain().removable();
        for (Point p : removable) {
            toTest.add(getMountain().getCase(p.x, p.y));
        }
        toTest.addAll(getAdditional());
        for (Color c : toTest) {
            if (getMountain().compatible(c).size() >= 1) {
                playable.add(c);
            }
        }
        return playable;
    }
}
