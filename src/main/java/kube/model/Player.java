package kube.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.awt.Point;

public class Player {

    int id, whiteUsed;
    String name;
    Mountain mountain;
    ArrayList<Color> additionals;
    HashMap<Color, Integer> avalaibleToBuild;

    // Constructor
    public Player(int id) {
        setId(id);
        setWhiteUsed(0);
        setMountain(new Mountain(6));
        clearMountain();
        setAdditionals(new ArrayList<Color>());
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

    public void setAdditionals(ArrayList<Color> additionals) {
        this.additionals = additionals;
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
        if (this.name == null) {
            return "Joueur " + getId();
        }
        return this.name;
    }

    public ArrayList<Color> getAdditionals() {
        return this.additionals;
    }

    public HashMap<Color, Integer> getAvalaibleToBuild() {
        return this.avalaibleToBuild;
    }

    // Methods
    public void addToAdditionals(Color color) {
        getAdditionals().add(color);
    }

    public Color removeFromAdditionals(int pos) {
        return getAdditionals().remove(pos);
    }

    public boolean buildToMoutain(Point point, Color color) {
        return buildToMoutain(point.x, point.y, color);
    }

    public boolean buildToMoutain(int l, int c, Color color) {

        if (l < 0 || c < 0 || l < c || l >= getMountain().getBaseSize()) {
            return false;
        }

        Color mountainColor = getMountain().getCase(l, c);
        if (getAvalaibleToBuild().get(color) > 0) {
            
            getMountain().setCase(l, c, color);

            if (mountainColor != Color.EMPTY) {
                getAvalaibleToBuild().put(mountainColor, getAvalaibleToBuild().get(mountainColor) + 1);
            }

            getAvalaibleToBuild().put(color, getAvalaibleToBuild().get(color) - 1);
            return true;
        }

        return false;
    }

    public Color unbuildFromMoutain(int l, int c) {

        if (l < 0 || c < 0 || l < c || l >= getMountain().getBaseSize()) {
            return Color.EMPTY;
        }

        Color mountainColor = getMountain().getCase(l, c);
        if (mountainColor != Color.EMPTY) {
            getMountain().remove(l, c);
            getAvalaibleToBuild().put(mountainColor, getAvalaibleToBuild().get(mountainColor) + 1);
            return mountainColor;
        }

        return mountainColor;
    }



    public boolean isAvailableToBuild(Color c) {
        return getAvalaibleToBuild().get(c) > 0;
    }

    public Color removeFromMountain(Point point) {
        return removeFromMountain(point.x, point.y);
    }

    public Color removeFromMountain(int l, int c) {
        Color col = getMountain().getCase(l, c);
        getMountain().remove(l, c);
        return col;
    }

    public void removeFromMountainToAvailableToBuild(Point p) {
        removeFromMountainToAvailableToBuild(p.x, p.y);
    }

    public void removeFromMountainToAvailableToBuild(int l, int c) {
        Color color;
        if ((color = getMountain().getCase(l, c)) != Color.EMPTY) {
            getMountain().remove(l, c);
            getAvalaibleToBuild().put(color, getAvalaibleToBuild().get(color) + 1);
        }
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
        for (Color c : getAdditionals()) {
            s += c.toString() + ",";
        }
        if (getAdditionals().size() > 0)
            s = s.substring(0, s.length() - 1);
        s += "}";
        return s;
    }

    public String toString() {
        String s = getName() + ":\n";
        s += getMountain().toString();
        s += "\nAdditionels: ";
        for (Color c : getAdditionals()) {
            s += c.forDisplay() + " ";
        }
        s+= "\n";
        return s;
    }

    public HashSet<Color> getPlayableColors() {

        HashSet<Color> playable = new HashSet<>();
        HashSet<Color> toTest = new HashSet<>();
        ArrayList<Point> removable = getMountain().removable();

        for (Point p : removable) {

            toTest.add(getMountain().getCase(p.x, p.y));
        }

        toTest.addAll(getAdditionals());

        for (Color c : toTest) {

            if (getMountain().compatible(c).size() >= 1) {

                playable.add(c);
            }
        }
        return playable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player p = (Player) o;
        if (getId() != p.getId()){
            return false;
        }
        return getMountain().equals(p.getMountain());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMountain(), getId());
    }

    @Override
    public Player clone(){
        Player p = new Player(getId());
        p.setAdditionals(new ArrayList<>(getAdditionals()));
        p.setName(getName());
        p.setWhiteUsed(getWhiteUsed());
        p.setMountain(getMountain().clone());
        return p;
    }
}
