package kube.model.action;

// Import java class
import java.io.Serializable;

public class Action implements Serializable {

    /**********
     * ATTRIBUTES
     **********/

    private ActionType type;
    private Object data;
    private int playerId;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of Action class
     * 
     * @param type     the type of the action
     * @param data     the Object data
     * @param playerId the id of the player that perform the action
     */
    public Action(ActionType type, Object data, int playerId) {
        this.type = type;
        this.data = data;
        this.playerId = playerId;
    }

    /**
     * Constructor of Action class
     * 
     * @param type the type of the action
     * @param data the Object data
     */
    public Action(ActionType type, Object data) {
        this(type, data, 0);
    }

    /**
     * Constructor of Action class
     * 
     * @param type the type of the action
     */
    public Action(ActionType type) {
        this(type, null);
    }

    /**********
     * SETTERS
     **********/

    public final void setType(ActionType type) {
        this.type = type;
    }

    public final void setData(Object data) {
        this.data = data;
    }

    public final void setPlayer(int p) {
        this.playerId = p;
    }

    /**********
     * GETTERS
     **********/

    public ActionType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public int getPlayer() {
        return playerId;
    }

    /**********
     * METHOD
     **********/

    @Override
    public String toString() {
        String s = "Action{type = " + type;
        if (data != null) {
            s += ", data = " + data.toString();
        }
        s += ", id = " + getPlayer();
        s += "}";
        return s;
    }
}
