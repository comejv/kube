package kube.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import kube.model.ai.abstractAI;

import java.awt.Point;

public class Player {

    /**********
     * ATTRIBUTES
     **********/

    String name;
    int id, whiteUsed;
    Mountain mountain;
    boolean hasValidateBuilding;
    ArrayList<Color> additionals;
    HashMap<Color, Integer> avalaibleToBuild;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class Player
     * 
     * @param id the player id
     */
    public Player(int id) {

        setId(id);
        setWhiteUsed(0);
        setMountain(new Mountain(6));
        clearMountain();
        setAdditionals(new ArrayList<Color>());
        setHasValidateBuilding(false);
    }

    /**********
     * SETTER
     **********/

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

    public void setAvailableToBuild(HashMap<Color, Integer> avalaibleToBuild) {
        this.avalaibleToBuild = avalaibleToBuild;
    }

    public void setHasValidateBuilding(boolean hasValidateBuilding) {
        this.hasValidateBuilding = hasValidateBuilding;
    }

    /**********
     * GETTER
     **********/

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

    public boolean getHasValidateBuilding() {
        return hasValidateBuilding;
    }

    public boolean isAI() {
        return false;
    }

    public abstractAI getAI() {
        return null;
    }

    /**********
     * BEFORE HAS VALIDATE BUILDING METHODS
     **********/

    /**
     * Check if the player can build a cube of the given color
     * 
     * @param c the color to check
     * @return true if the player can build a cube of the given color, false
     *         otherwise
     */
    public boolean isAvailableToBuild(Color c) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player has already validate his building");
        }

        return getAvalaibleToBuild().get(c) > 0;
    }

    /**
     * Add a color to the player's mountain using available colors
     * 
     * @param point the position to build
     * @param color the color to build
     * @return true if the color has been built, false otherwise
     */
    public boolean addToMountainFromAvailableToBuild(Point point, Color color) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player has already validate his building");
        }

        return addToMountainFromAvailableToBuild(point.x, point.y, color);
    }

    /**
     * Add a color to the player's mountain using available colors
     * 
     * @param l     the x position to build
     * @param c     the y position to build
     * @param color the color to build
     * @return true if the color has been built, false otherwise
     */
    public boolean addToMountainFromAvailableToBuild(int l, int c, Color color) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player has already validate his building");
        }

        Color mountainColor;
        Integer availableNumber;
        boolean isInMountain;

        isInMountain = l >= 0 && l < getMountain().getBaseSize() && c >= 0 && c <= l;
        if (getHasValidateBuilding() || !isInMountain) {
            return false;
        }

        mountainColor = getMountain().getCase(l, c);
        if (getAvalaibleToBuild().get(color) > 0) {

            getMountain().setCase(l, c, color);
            if (mountainColor != Color.EMPTY) {

                availableNumber = getAvalaibleToBuild().get(mountainColor) + 1;
                getAvalaibleToBuild().put(mountainColor, availableNumber);
            }

            getAvalaibleToBuild().put(color, getAvalaibleToBuild().get(color) - 1);
            return true;
        }

        return false;
    }

    /**
     * Remove a color from the player's mountain to the available to build
     * 
     * @param point the position to remove
     * @return the color removed
     */
    public Color removeFromMountainToAvailableToBuild(Point point) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player has already validate his building");
        }

        return removeFromMountainToAvailableToBuild(point.x, point.y);
    }

    /**
     * Remove a color from the player's mountain to the available to build
     * 
     * @param x the x position to remove
     * @param y the y position to remove
     * @return the color removed
     */
    public Color removeFromMountainToAvailableToBuild(int x, int y) throws UnsupportedOperationException {
        
        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player has already validate his building");
        }

        Color mountainColor;

        if (hasValidateBuilding || x < 0 || y < 0 || x < y || x >= getMountain().getBaseSize()) {
            return Color.EMPTY;
        }

        mountainColor = getMountain().getCase(x, y);
        if (mountainColor != Color.EMPTY) {
            getMountain().remove(x, y);
            getAvalaibleToBuild().put(mountainColor, getAvalaibleToBuild().get(mountainColor) + 1);
            return mountainColor;
        }

        return mountainColor;
    }

    /**
     * Validdates the building of the player if the mountain is full
     * 
     * @return true if the building has been validated, false otherwise
     */
    public boolean validateBuilding() throws UnsupportedOperationException {
        
        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player has already validate his building");
        }

        if (getMountain().isFull()) {
            setHasValidateBuilding(true);
        }

        return getHasValidateBuilding();
    }

    /**********
     * AFTER HAS VALIDATE BUILDING METHODS
     **********/

    /**
     * Add a color to the player's additionals
     * 
     * @param color the color to add
     */
    public void addToAdditionals(Color color) throws UnsupportedOperationException {
        
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player hasn't validate his building");
        }

        getAdditionals().add(color);
    }

    /**
     * Remove a color from the player's additionals
     * 
     * @param pos the index of the color to remove
     * @return the color removed
     */
    public Color removeFromAdditionals(int pos) throws UnsupportedOperationException {
        
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player hasn't validate his building");
        }

        return getAdditionals().remove(pos);
    }

    /**
     * Remove a color from the player's mountain with the given position
     * 
     * @param point the position to remove
     * @return the color removed
     */
    public Color removeFromMountain(Point point) throws UnsupportedOperationException {
        
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player hasn't validate his building");
        }

        return removeFromMountain(point.x, point.y);
    }

    /**
     * Remove a color from the player's mountain with the given position
     * 
     * @param l the x position to remove
     * @param c the y position to remove
     * @return the color removed
     */
    public Color removeFromMountain(int l, int c) throws UnsupportedOperationException {
        
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player hasn't validate his building");
        }

        Color col;
        col = getMountain().getCase(l, c);
        getMountain().remove(l, c);
        return col;
    }

    /**
     * Give the list of playable colors by player
     * 
     * @return the list of playable colors
     */
    public HashSet<Color> getPlayableColors() throws UnsupportedOperationException {
        
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException("Forbidden operation, the player hasn't validate his building");
        }

        HashSet<Color> playable;
        HashSet<Color> toTest;
        ArrayList<Point> removable;

        toTest = new HashSet<>();
        removable = new ArrayList<>();
        for (Point p : removable) {
            toTest.add(getMountain().getCase(p.x, p.y));
        }

        toTest.addAll(getAdditionals());

        playable = new HashSet<>();
        for (Color c : toTest) {
            if (getMountain().compatible(c).size() >= 1) {
                playable.add(c);
            }
        }

        return playable;
    }

    /**********
     * OTHER METHODS
     **********/

    /**
     * Clear the mountain of the player
     * 
     * @return void
     */
    public void clearMountain() {
        getMountain().clear();
    }

    /**
     * Check if the mountain of the player is full
     * 
     * @return true if the mountain is full, false otherwise
     */
    public boolean isMountainFull() {
        return getMountain().isFull();
    }

    /**
     * Check if the mountain of the player is empty
     * 
     * @return true if the mountain is empty, false otherwise
     */
    public boolean isMountainEmpty() {
        return getMountain().isEmpty();
    }

    /**
     * Return a string representing the player for saving it
     * 
     * @return a string representing the player
     */
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

    @Override
    public String toString() {

        String s = getName() + ":\n";
        s += getMountain().toString();
        s += "\nAdditionels: ";
        for (Color c : getAdditionals()) {
            s += c.forDisplay() + " ";
        }
        s += "\n";
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
        Player p = (Player) o;
        if (getId() != p.getId()) {
            return false;
        }
        return getMountain().equals(p.getMountain());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMountain(), getId());
    }

    @Override
    public Player clone() {

        Player p = new Player(getId());
        p.setAdditionals(new ArrayList<>(getAdditionals()));
        p.setName(getName());
        p.setWhiteUsed(getWhiteUsed());
        p.setMountain(getMountain().clone());
        return p;
    }
}
