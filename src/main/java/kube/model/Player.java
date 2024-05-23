package kube.model;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

import kube.model.ai.MiniMaxAI;

/**********
 * JSON SERIALIZATION/DESERIALIZATION ANNOTATIONS
 **********/

@JsonSerialize(using = Player.PlayerSerializer.class)
@JsonDeserialize(using = Player.PlayerDeserializer.class)
public class Player implements Serializable {

    /**
     * ********
     * ATTRIBUTES ********
     */
    private String name;
    private int id, whiteUsed;
    private Mountain initialMountain, mountain;
    private boolean validate, hasValidateBuilding;
    private ArrayList<ModelColor> additionals;
    private HashMap<ModelColor, Integer> avalaibleToBuild;

    /**********
     * CONSTRUCTOR
     **********/
    private HashMap<ModelColor, Integer> usedPiece;

    /**
     * ********
     * CONSTRUCTORS ********
     */
    /**
     * Constructor of the class Player
     *
     * @param id the player id
     */
    public Player(int id) {

        this.id = id;
        this.whiteUsed = 0;
        this.id = id;
        this.whiteUsed = 0;
        this.mountain = new Mountain(6);
        clearMountain();
        this.additionals = new ArrayList<>();
        this.hasValidateBuilding = false;
        this.usedPiece = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColoredAndJokers()){
            usedPiece.put(c, 0);
        }
    }

    /**********
     * SERIALIZER
     **********/

    public static class PlayerSerializer extends JsonSerializer<Player> {
        @Override
        public void serialize(Player player, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            // Serialize the name attributes
            jsonGenerator.writeStringField("name", player.getName());
            // Serialize the id attribute
            jsonGenerator.writeNumberField("id", player.getId());
            // Serialize the hasValidateBuilding attribute
            jsonGenerator.writeBooleanField("has_validate_building", player.getHasValidateBuilding());
    
            if (!player.getHasValidateBuilding()) {
                // Serialize the mountain attribute
                jsonGenerator.writeObjectField("mountain", player.getMountain());
                // Serialize the availableToBuild attribute
                jsonGenerator.writeObjectFieldStart("available_to_build");
                // Serialize the availableToBuild hashmap
                for (Map.Entry<ModelColor, Integer> entry : player.getAvailaibleToBuild().entrySet()) {
                    jsonGenerator.writeNumberField(entry.getKey().toString(), entry.getValue());
                }
                jsonGenerator.writeEndObject();
            } else {
                // Serialize the initialMountain attribute
                jsonGenerator.writeObjectField("initial_mountain", player.getInitialMountain());
            }
    
            jsonGenerator.writeEndObject();
        }
    }

    /**********
     * DESERIALIZER
     **********/

    public static class PlayerDeserializer extends JsonDeserializer<Player> {
        @Override
        public Player deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            
            Player player = new Player(0);

            while(!jsonParser.isClosed()){
                JsonToken jsonToken = jsonParser.nextToken();

                if(JsonToken.FIELD_NAME.equals(jsonToken)){
                    String fieldName = jsonParser.currentName();
                    jsonParser.nextToken();

                    switch(fieldName){
                        case "name":
                            player.name = jsonParser.getValueAsString();
                            break;
                        case "id":
                            player.id = jsonParser.getValueAsInt();
                            break;
                        case "has_validate_building":
                            player.hasValidateBuilding = jsonParser.getValueAsBoolean();
                            break;
                        case "mountain":
                            player.mountain = jsonParser.readValueAs(Mountain.class);
                            break;
                        case "available_to_build":
                            // Convert the json object to a hashmap
                            TypeReference<HashMap<ModelColor, Integer>> typeRef = new TypeReference<HashMap<ModelColor, Integer>>(){};
                            HashMap<ModelColor, Integer> availableToBuild = jsonParser.readValueAs(typeRef);
                            player.avalaibleToBuild = new HashMap<>();
                            // Filling the player availableToBuild hashmap
                            for (Map.Entry<ModelColor, Integer> entry : availableToBuild.entrySet()){
                                player.avalaibleToBuild.put(entry.getKey(), entry.getValue());
                            }
                            break;
                        case "initial_mountain":
                            player.initialMountain = jsonParser.readValueAs(Mountain.class);
                            player.mountain = player.initialMountain.clone();
                            break;
                    }
                }
            }
            return player;
        }
    }

    /**********
     * SETTERS
     **********/

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public void setInitialMountain(Mountain initialMountain) {
        this.initialMountain = initialMountain;
    }

    @JsonSetter("mountain")
    public void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public void setAdditionals(ArrayList<ModelColor> additionals) {
        this.additionals = additionals;
    }

    @JsonSetter("available_to_build")
    public void setAvailableToBuild(HashMap<ModelColor, Integer> avalaibleToBuild) {
        this.avalaibleToBuild = avalaibleToBuild;
    }

    @JsonSetter("has_validate_building")
    public void setHasValidateBuilding(boolean hasValidateBuilding) {
        this.hasValidateBuilding = hasValidateBuilding;
    }


    public void setusedPiece(HashMap<ModelColor, Integer> usedPiece){
        this.usedPiece = usedPiece;
    }

    /**********
     * GETTERS
     *********/
    
    public int getId() {
        return this.id;
    }

    public int getWhiteUsed() {
        return this.usedPiece.get(ModelColor.WHITE);
    }

    @JsonGetter("initial_mountain")
    public Mountain getInitialMountain() {
        return this.initialMountain;
    }

    @JsonGetter("mountain")
    public Mountain getMountain() {
        return this.mountain;
    }

    @JsonGetter("name")
    public String getName() {
        if (this.name == null) {
            return "Joueur " + getId();
        }
        return this.name;
    }

    public ArrayList<ModelColor> getAdditionals() {
        return this.additionals;
    }

    @JsonGetter("available_to_build")
    public HashMap<ModelColor, Integer> getAvailaibleToBuild() {
        return this.avalaibleToBuild;
    }

    @JsonGetter("has_validate_building")
    public boolean getHasValidateBuilding() {
        return hasValidateBuilding;
    }

    @JsonIgnore
    public MiniMaxAI getAI() {
        return null;
    }

    public HashMap<ModelColor, Integer> getUsedPiece(){
        return usedPiece;
    }


    /**
     * ********
     * BEFORE HAS VALIDATE BUILDING METHODS ********
     */
    /**
     * Check if the player can build a cube of the given color
     *
     * @param c the color to check
     * @return true if the player can build a cube of the given color, false
     * otherwise
     */
    @JsonIgnore
    public boolean isAvailableToBuild(ModelColor c) throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        return getAvailaibleToBuild().get(c) > 0;
    }

    /**
     * Add a color to the player's mountain using available colors
     *
     * @param point the position to build
     * @param color the color to build
     * @return true if the color has been built, false otherwise
     */
    @JsonIgnore
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
     * @param x the x position to build
     * @param y the y position to build
     * @param color the color to build
     * @return true if the color has been built, false otherwise
     */
    @JsonIgnore
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
        if (getAvailaibleToBuild().get(color) > 0) {

            getMountain().setCase(x, y, color);
            if (mountainColor != ModelColor.EMPTY) {

                availableNumber = getAvailaibleToBuild().get(mountainColor) + 1;
                getAvailaibleToBuild().put(mountainColor, availableNumber);
            }

            getAvailaibleToBuild().put(color, getAvailaibleToBuild().get(color) - 1);
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
    @JsonIgnore
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
    @JsonIgnore
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
            getAvailaibleToBuild().put(mountainColor, getAvailaibleToBuild().get(mountainColor) + 1);
            return mountainColor;
        }

        return mountainColor;
    }

    /**
     * Validdates the building of the player if the mountain is full
     *
     * @return true if the building has been validated, false otherwise
     */
    @JsonIgnore
    public boolean validateBuilding() throws UnsupportedOperationException {

        if (getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "Forbidden operation, the player has already validate his building");
        }

        if (getMountain().isFull()) {
            setHasValidateBuilding(true);
            setInitialMountain(getMountain().clone());
        }

        //return getHasValidateBuilding();
        return getMountain().isFull();
    }

    /**
     * ********
     * AFTER HAS VALIDATE BUILDING METHODS ********
     */
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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

    public void addUsedPiece(ModelColor c){
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "removeFromAdditionals: Forbidden operation, the player hasn't validate his building");
        }
        usedPiece.put(c, usedPiece.get(c) + 1);
    }

    public void removeUsedPiece(ModelColor c){
        if (!getHasValidateBuilding()) {
            throw new UnsupportedOperationException(
                    "removeFromAdditionals: Forbidden operation, the player hasn't validate his building");
        }
        usedPiece.put(c, usedPiece.get(c) - 1);
    }

    /**********
     * OTHER METHODS
     **********/

    /**
     * Check if the player is an AI
     * 
     * @return true if the player is an AI, false otherwise
     */
    @JsonIgnore
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
    @JsonIgnore
    public boolean isMountainFull() {
        return getMountain().isFull();
    }

    /**
     * Check if the mountain of the player is empty
     *
     * @return true if the mountain is empty, false otherwise
     */
    @JsonIgnore
    public boolean isMountainEmpty() {
        return getMountain().isEmpty();
    }

    /**
     * Return a string representing the player for saving it
     *
     * @return a string representing the player
     */
    /*public String forSave() {

        String save;
        boolean addedAvailableToBuild;

        save = "{" + getId() + " " + getName() + " ";

        if (!hasValidateBuilding) {
            addedAvailableToBuild = false;
            save += getMountain().forSave() + " ";
            save += "[";
            for (Map.Entry<ModelColor, Integer> entry : getAvailaibleToBuild().entrySet()) {
                save += entry.getKey().forSave() + ":" + entry.getValue() + ",";
                addedAvailableToBuild = true;
            }
            if (addedAvailableToBuild) {
                save = save.substring(0, save.length() - 1);
            }
            save += "]";
        }
        else {
            save += getInitialMountain().forSave() + " ";
            save += "[";
            save += "]";
        }

        save += "}";
        return save;
    }*/

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
        p.setusedPiece(new HashMap<>(getUsedPiece()));
        p.setMountain(getMountain().clone());
        p.setHasValidateBuilding(getHasValidateBuilding());
        return p;
    }
}
