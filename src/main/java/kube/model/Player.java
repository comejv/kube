package kube.model;

// Import model classes
import kube.model.ai.MiniMaxAI;

// Import java classes
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Player implements Serializable {

    /**********
     * ATTRIBUTES
     **********/

    private String name;
    private int id;
    private Mountain mountain;
    private boolean isMountainValidated;
    private ArrayList<ModelColor> additionals;
    private HashMap<ModelColor, Integer> avalaibleToBuild, usedPiece;
    private MiniMaxAI ai;

    /***********
     * CONSTRUCTORS
     ***********/

    /**
     * Constructor of the class Player
     *
     * @param id the player id
     */
    public Player(int id) {

        this.id = id;
        this.mountain = new Mountain(6);
        clearMountain();
        this.additionals = new ArrayList<>();
        this.isMountainValidated = false;
        this.usedPiece = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            usedPiece.put(c, 0);
        }
    }

    /**********
     * SETTERS
     **********/

    public final void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final void setAdditionals(ArrayList<ModelColor> additionals) {
        this.additionals = additionals;
    }

    public final void setAvailableToBuild(HashMap<ModelColor, Integer> avalaibleToBuild) {
        this.avalaibleToBuild = avalaibleToBuild;
    }

    public final void setIsMountainValidated(boolean isMountainValidated) {
        this.isMountainValidated = isMountainValidated;
    }

    public final void setUsedPiece(HashMap<ModelColor, Integer> usedPiece) {
        this.usedPiece = usedPiece;
    }

    public final void setAI(MiniMaxAI ai) {
        this.ai = ai;
    }

    /**********
     * GETTERS
     **********/

    public int getId() {
        return this.id;
    }

    public int getWhiteUsed() {
        return this.usedPiece.get(ModelColor.WHITE);
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

    public HashMap<ModelColor, Integer> getAvailableToBuild() {
        return this.avalaibleToBuild;
    }

    public boolean getIsMountainValidated() {
        return isMountainValidated;
    }

    public MiniMaxAI getAI() {
        return ai;
    }

    public HashMap<ModelColor, Integer> getUsedPiece() {
        return usedPiece;
    }

    /**********
     * BEFORE HAS VALIDATE BUILDING METHODS
     **********/

    /**
     * Check if the player can build a cube of the given color
     *
     * @param color the color to check
     * @return true if the player can build a cube of the given color, false
     *         otherwise
     */
    public boolean isAvailableToBuild(ModelColor color) throws UnsupportedOperationException {

        if (getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        return getAvailableToBuild().get(color) > 0;
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

        if (getIsMountainValidated()) {
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

        if (getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        ModelColor mountainColor;
        Integer availableNumber;
        boolean isInMountain;

        isInMountain = x >= 0 && x < getMountain().getBaseSize() && y >= 0 && y <= x;
        if (getIsMountainValidated() || !isInMountain) {
            return false;
        }

        mountainColor = getMountain().getCase(x, y);
        if (color == ModelColor.EMPTY || getAvailableToBuild().get(color) > 0) {
            getMountain().setCase(x, y, color);
            if (mountainColor != ModelColor.EMPTY) {
                availableNumber = getAvailableToBuild().get(mountainColor) + 1;
                getAvailableToBuild().put(mountainColor, availableNumber);
            }
            if (color != ModelColor.EMPTY) {
                getAvailableToBuild().put(color, getAvailableToBuild().get(color) - 1);
            }
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

        if (getIsMountainValidated()) {
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

        if (getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        ModelColor mountainColor;

        if (isMountainValidated || x < 0 || y < 0 || x < y || x >= getMountain().getBaseSize()) {
            return ModelColor.EMPTY;
        }

        mountainColor = getMountain().getCase(x, y);

        if (mountainColor != ModelColor.EMPTY) {
            getMountain().remove(x, y);
            getAvailableToBuild().put(mountainColor, getAvailableToBuild().get(mountainColor) + 1);
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

        if (getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        if (getMountain().isFull()) {
            setIsMountainValidated(true);
        }

        return getMountain().isFull();
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

        if (!getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "addToAdditionals: Forbidden operation, the player hasn't validate his building");
        }

        getAdditionals().add(color);
    }

    /**
     * Remove a color from the player's additionals
     *
     * @param position the index of the color to remove
     * @return the color removed
     */
    public ModelColor removeFromAdditionals(int position) throws UnsupportedOperationException {

        if (!getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "removeFromAdditionals: Forbidden operation, the player hasn't validate his building");
        }

        return getAdditionals().remove(position);
    }

    /**
     * Remove a color from the player's mountain with the given position
     *
     * @param position the position to remove
     * @return the color removed
     */
    public ModelColor removeFromMountain(Point position) throws UnsupportedOperationException {

        if (!getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "removeFromMountain: Forbidden operation, the player hasn't validate his building");
        }

        return removeFromMountain(position.x, position.y);
    }

    /**
     * Remove a color from the player's mountain with the given position
     *
     * @param x the x position to remove
     * @param y the y position to remove
     * @return the color removed
     */
    public ModelColor removeFromMountain(int x, int y) throws UnsupportedOperationException {

        ModelColor color;

        if (!getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "removeFromMountain: Forbidden operation, the player hasn't validate his building");
        }

        color = getMountain().getCase(x, y);
        getMountain().remove(x, y);
        return color;
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

    /**
     * Add a color to the player's used pieces
     *
     * @param color the color to add
     * @return void
     */
    public void addUsedPiece(ModelColor color) {

        if (!getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "removeFromAdditionals: Forbidden operation, the player hasn't validate his building");
        }

        usedPiece.put(color, usedPiece.get(color) + 1);
    }

    /**
     * Remove a color from the player's used pieces
     *
     * @param color the color to remove
     * @return void
     */
    public void removeUsedPiece(ModelColor color) {
        if (!getIsMountainValidated()) {
            throw new UnsupportedOperationException(
                    "removeFromAdditionals: Forbidden operation, the player hasn't validate his building");
        }
        usedPiece.put(color, usedPiece.get(color) - 1);
    }

    /**********
     * OTHER METHODS
     **********/

    /**
     * Check if the player is an AI
     * 
     * @return true if the player is an AI, false otherwise
     */
    public boolean isAI() {
        return false;
    }

    /**
     * Clear the mountain of the player
     *
     * @return void
     */
    public final void clearMountain() {
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
        if (!getIsMountainValidated()) {
            p.setAvailableToBuild(new HashMap<>(getAvailableToBuild()));
        }
        p.setName(getName());
        p.setUsedPiece(new HashMap<>(getUsedPiece()));
        p.setMountain(getMountain().clone());
        p.setIsMountainValidated(getIsMountainValidated());
        return p;
    }
}
