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

    public void addMountain(Point point, Color color) {
        addMountain(point, color);
    }

    public void addMountain(int l, int c, Color color) {
        getMountain().addPoint(l,c,color);
    }


    public void removeMountain(Point point) {
        getMountain().removePoint(point);
    }

    public void clearMountain() {
        getMountain().clear();
    }

    public boolean isMountainFull() {
        return getMountain().isFull();
    }
}
