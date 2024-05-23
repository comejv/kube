package kube.model.action;

import java.io.Serializable;

public class Action implements Serializable {
    private ActionType type;
    private Object data;

    public Action(ActionType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Action(ActionType type) {
        this(type, null);
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String s = "Action{type = " + type;
        if (data != null) {
            s += ", data = " + data.toString();
        }
        s += "}";
        return s;
    }
}
