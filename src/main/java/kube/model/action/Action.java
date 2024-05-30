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
     * Constructor of the class Action
     * 
     * @param type     the type of the action
     * @param data     the data of the action
     * @param playerId the id of the player
     */
    public Action(ActionType type, Object data, int playerId) {
        this.type = type;
        this.data = data;
        this.playerId = playerId;
    }

    /**
     * Constructor of the class Action with playerId = 0
     * 
     * @param type the type of the action
     * @param data the data of the action
     */
    public Action(ActionType type, Object data) {
        this(type, data, 0);
    }

    /**
     * Constructor of the class Action without data and playerId = 0
     * 
     * @param type the type of the action
     */
    public Action(ActionType type) {
        this(type, null);
    }

    /**********
     * SETTER
     **********/

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

    public int getPlayerId() {
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
        s += ", id = " + getPlayerId();
        s += "}";
        return s;
    }
}
