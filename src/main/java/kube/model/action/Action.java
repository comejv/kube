package kube.model.action;

public class Action{
    public static final int SHOW_ALL = 0;
    public static final int SHOW_MOUNTAIN = 1;
    public static final int SWAP = 2;
    public static final int SHUFFLE = 3;
    public static final int VALIDATE = 4;
    public static final int PLAY = 5;
    public static final int MOVE = 6;
    public static final int UNDO = 7;
    public static final int REDO = 8;


    private int type;
    private Object data;

    public Action(int type, Object data){
        setType(type);
        setData(data);
    }

    public Action(int type){
        this(type, null);
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }

    public String toString(){
        String s = "Action{type = " + type;
        if(data != null){
            s += ", data = " + data.toString();
        }
        s += "}";
        return s;
    }
        
}