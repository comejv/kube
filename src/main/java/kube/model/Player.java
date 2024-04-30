package kube.model;

import java.util.ArrayList;
import java.awt.Point;

public class Player {
    
    int id;
    Mountain mountain;
    ArrayList<Color> additional;

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

    // Methods

    public void addAdditional(Color color) {
        getAdditional().add(color);
    }

    public void removeAdditional(Color color) {
        getAdditional().remove(color);
    }

    public void addToMountain(Point point, Color color) {
        //TODO
    }

    public void addToMountain(int l, int c, Color color) {
        addToMountain(new Point(l, c), color);
    }


    public void removeFromMountain(Point point) {
        //TODO
    }

    public void removeFromMountain(int l, int c) {
        removeFromMountain(new Point(l,c));
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
