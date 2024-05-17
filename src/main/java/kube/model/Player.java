package kube.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import kube.model.ai.MiniMaxAI;

import java.awt.Point;
import java.io.Serializable;

public class Player implements Serializable {

    /**********
     * ATTRIBUTES
     **********/

    private String name;
    private int id, whiteUsed;
    private Mountain initialMountain, mountain;
    private boolean hasValidateBuilding;
    private ArrayList<ModelColor> additionals;
    private HashMap<ModelColor, Integer> avalaibleToBuild;

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
        setAdditionals(new ArrayList<ModelColor>());
        setHasValidateBuilding(false);
    }

    /**********
     * SETTERS
     **********/

    public void setId(int id) {
        this.id = id;
    }

    public void setWhiteUsed(int whiteUsed) {
        this.whiteUsed = whiteUsed;
    }

    public void setInitialMountain(Mountain initialMountain) {
        this.initialMountain = initialMountain;
    }

    public void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdditionals(ArrayList<ModelColor> additionals) {
        this.additionals = additionals;
    }

    public void setAvailableToBuild(HashMap<ModelColor, Integer> avalaibleToBuild) {
        this.avalaibleToBuild = avalaibleToBuild;
    }

    public void setHasValidateBuilding(boolean hasValidateBuilding) {
        this.hasValidateBuilding = hasValidateBuilding;
    }

    /**********
     * GETTERS
     **********/

    public int getId() {
        return this.id;
    }

    public int getWhiteUsed() {
        return this.whiteUsed;
    }

    public Mountain getInitialMountain() {
        return this.initialMountain;
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

    public ArrayList<ModelColor> getAdditionals() {
        return this.additionals;
    }

    public HashMap<ModelColor, Integer> getAvalaibleToBuild() {
        return this.avalaibleToBuild;
    }

    public boolean getHasValidateBuilding() {
        return hasValidateBuilding;
    }

    public boolean isAI() {
        return false;
    }

    public MiniMaxAI getAI() {
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
    public boolean isAvailableToBuild(ModelColor c) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
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
    public boolean addToMountainFromAvailableToBuild(Point point, ModelColor color)
            throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        return addToMountainFromAvailableToBuild(point.x, point.y, color);
    }

    /**
     * Add a color to the player's mountain using available colors
     * 
     * @param x     the x position to build
     * @param y     the y position to build
     * @param color the color to build
     * @return true if the color has been built, false otherwise
     */
    public boolean addToMountainFromAvailableToBuild(int x, int y, ModelColor color)
            throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        ModelColor mountainColor;
        Integer availableNumber;
        boolean isInMountain;

        isInMountain = x >= 0 && x < getMountain().getBaseSize() && y >= 0 && y <= x;
        if (getHasValidateBuilding() || !isInMountain) {
            return false;
        }

        mountainColor = getMountain().getCase(x, y);
        if (getAvalaibleToBuild().get(color) > 0) {

            getMountain().setCase(x, y, color);
            if (mountainColor != ModelColor.EMPTY) {

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
    public ModelColor removeFromMountainToAvailableToBuild(Point point) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
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
    public ModelColor removeFromMountainToAvailableToBuild(int x, int y) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        ModelColor mountainColor;

        if (hasValidateBuilding || x < 0 || y < 0 || x < y || x >= getMountain().getBaseSize()) {
            return ModelColor.EMPTY;
        }

        mountainColor = getMountain().getCase(x, y);
        if (mountainColor != ModelColor.EMPTY) {
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
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        if (getMountain().isFull()) {
            setHasValidateBuilding(true);
            setInitialMountain(getMountain().clone());
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
    public void addToAdditionals(ModelColor color) throws UnsupportedOperationException {

        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "addToAdditionals: Forbidden operation, the player hasn't validate his building");
        }

        getAdditionals().add(color);
    }

    /**
     * Remove a color from the player's additionals
     * 
     * @param pos the index of the color to remove
     * @return the color removed
     */
    public ModelColor removeFromAdditionals(int pos) throws UnsupportedOperationException {

        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "removeFromAdditionals: Forbidden operation, the player hasn't validate his building");
        }

        return getAdditionals().remove(pos);
    }

    /**
     * Remove a color from the player's mountain with the given position
     * 
     * @param point the position to remove
     * @return the color removed
     */
    public ModelColor removeFromMountain(Point point) throws UnsupportedOperationException {

        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "removeFromMountain: Forbidden operation, the player hasn't validate his building");
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
    public ModelColor removeFromMountain(int l, int c) throws UnsupportedOperationException {

        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "removeFromMountain: Forbidden operation, the player hasn't validate his building");
        }

        ModelColor col;
        col = getMountain().getCase(l, c);
        getMountain().remove(l, c);
        return col;
    }

    /**
     * Give the list of playable colors by player
     * 
     * @return the list of playable colors
     */
    public HashSet<ModelColor> getPlayableColors() throws UnsupportedOperationException {

        HashSet<ModelColor> playable;
        HashSet<ModelColor> toTest;
        
        toTest = new HashSet<>();
        for (Point p : getMountain().removable()) {
            toTest.add(getMountain().getCase(p.x, p.y));
        }

        toTest.addAll(getAdditionals());

        playable = new HashSet<>();
        for (ModelColor c : toTest) {
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
        for (ModelColor c : getAdditionals()) {
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
        for (ModelColor c : getAdditionals()) {
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
        if (getId() != p.getId() && isAI() != p.isAI()) {
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
        p.setHasValidateBuilding(getHasValidateBuilding());
        return p;
    }
}
