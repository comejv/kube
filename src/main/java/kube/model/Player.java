package kube.model;

import java.util.ArrayList;
import java.awt.Point;

public class Player {
    
    int id;
    Mountain mountain;
    ArrayList<Color> additional;
    ArrayList<Color> avalaibleToBuild;
    // Constructor

    public Player(int id) {
        setId(id);
        setMountain(new Mountain(6));
        clearMountain();
        setAdditional(new ArrayList<Color>());
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    public void setAdditional(ArrayList<Color> additional) {
        this.additional = additional;
    }

    public void setAvalaibleToBuild(ArrayList<Color> avalaibleToBuild) {
        this.avalaibleToBuild = avalaibleToBuild;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public Mountain getMountain() {
        return this.mountain;
    }

    public ArrayList<Color> getAdditional() {
        return this.additional;
    }

    public ArrayList<Color> getAvalaibleToBuild() {
        return this.avalaibleToBuild;
    }

    // Methods
    public void addAdditional(Color color) {
        getAdditional().add(color);
    }

    public Color removeAdditional(int pos) {
        return getAdditional().remove(pos);
    }

    public void addToMountain(Point point, int pos) {
        addToMountain(point.x, point.y, pos);
    }

    public void addToMountain(int l, int c,int pos) {
        getMountain().setCase(l, c, avalaibleToBuild.get(pos));
    }

    public Color removeFromMountain(Point point) {
        return removeFromMountain(point.x, point.y);
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
        s+= "\nMountain: " + getMountain().toString();
        s+= "\nAdditional: ";
        for (Color c : getAdditional()) {
            s += c.toString() + " ";
        }
        return s;
    }
}
