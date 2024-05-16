package kube.model.action;

public class Action{
    public static final int SHOW_ALL = 0;
    public static final int SHOW_MOUNTAIN = 1;
    public static final int SWAP = 2;
    public static final int SHUFFLE = 3;
    public static final int VALIDATE = 4;
    public static final int MOVE = 6;
    public static final int UNDO = 7;
    public static final int REDO = 8;
    public static final int PRINT_AI = 9;
    public static final int PRINT_COMMAND_ERROR = 10;
    public static final int PRINT_WAIT_COORDINATES = 11; 
    public static final int PRINT_GOODBYE = 12;
    public static final int PRINT_HELP = 13;
    public static final int PRINT_LIST_MOVES = 14;
    public static final int PRINT_MOVE = 15;
    public static final int PRINT_MOVE_ERROR = 16;
    public static final int PRINT_NEXT_PLAYER = 17;
    public static final int PRINT_PLAYER = 18;
    public static final int PRINT_PLAYER_NAME = 19;
    public static final int PRINT_RANDOM = 20;
    public static final int PRINT_REDO = 21;
    public static final int PRINT_REDO_ERROR = 22;
    public static final int PRINT_START = 23;
    public static final int PRINT_STATE = 24;
    public static final int PRINT_SWAP = 25;
    public static final int PRINT_SWAP_ERROR = 26;
    public static final int PRINT_SWAP_SUCCESS = 27;
    public static final int PRINT_UNDO = 28;
    public static final int PRINT_UNDO_ERROR = 29;
    public static final int PRINT_VALIDATE = 30;
    public static final int PRINT_WELCOME = 31;
    public static final int PRINT_WIN_MESSAGE = 32;
    public static final int PRINT_FORBIDDEN_ACTION = 33;
    public static final int PRINT_ASK_NB_PLAYERS = 34;
    public static final int PRINT_ASK_PLAYER_NAME = 35;
    public static final int PRINT_ASK_GAME_MODE = 36;

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