package kube.model.action.move;

// Import model classes
import kube.model.ModelColor;
import kube.model.Player;
// Import jackson classes
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
// Import java classes
import java.awt.Point;
import java.io.Serializable;

/**********
 * JSON SERIALIZATION/DESERIALIZATION ANNOTATIONS
 **********/

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MoveMW.class, name = "MW"),
        @JsonSubTypes.Type(value = MoveMM.class, name = "MM"),
        @JsonSubTypes.Type(value = MoveAM.class, name = "AM"),
        @JsonSubTypes.Type(value = MoveAW.class, name = "AW"),
        @JsonSubTypes.Type(value = MoveMA.class, name = "MA"),
        @JsonSubTypes.Type(value = MoveAA.class, name = "AA")
})
public abstract class Move implements Serializable {

    /**********
     * ATTRIBUTES
     **********/

    @JsonIgnore
    private Player player;

    @JsonProperty("color")
    private ModelColor color;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class Move for an empty move
     */
    public Move() {
        this.color = ModelColor.EMPTY;
    }

    /**
     * Constructor of the class Move
     * 
     * @param color
     */
    @JsonCreator
    public Move(@JsonProperty("color") ModelColor color) {
        this.color = color;
    }

    /**********
     * SETTERS
     **********/

    public final void setPlayer(Player player) {
        this.player = player;
    }

    @JsonSetter("color")
    public final void setColor(ModelColor color) {
        this.color = color;
    }

    /**********
     * GETTERS
     **********/

    public Player getPlayer() {
        return this.player;
    }

    @JsonProperty("color")
    public ModelColor getColor() {
        return this.color;
    }

    @JsonIgnore
    public Point getFrom() {
        return null;
    }

    @JsonIgnore
    public Point getTo() {
        return null;
    }

    /**********
     * METHODS
     **********/

    /**
     * Check if the move is from the additionals
     *
     * @return true if the move is from the additionals, false otherwise
     */
    @JsonIgnore
    public boolean isFromAdditionals() {
        return false;
    }

    /**
     * Check if the move is to the additionals (penality)
     * 
     * @return true if the move is to the additionals, false otherwise
     */
    @JsonIgnore
    public boolean isToAdditionals() {
        return false;
    }

    /**
     * Check if the move is a white move
     * 
     * @return true if the move is a white move, false otherwise
     */
    @JsonIgnore
    public boolean isWhite() {
        return false;
    }

    /**
     * Check if the move is a classic move
     * 
     * @return true if the move is a classic move, false otherwise
     */
    @JsonIgnore
    public boolean isClassicMove() {
        return false;
    }

    @Override
    public boolean equals(Object o) {

        Move that;

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        that = (Move) o;
        if (!this.getClass().equals(that.getClass()))
            return false;

        if (getFrom() != null ? !getFrom().equals(that.getFrom()) : that.getFrom() != null)
            return false;

        if (getTo() != null ? !getTo().equals(that.getTo()) : that.getTo() != null)
            return false;

        return getColor() == that.getColor();
    }
}
