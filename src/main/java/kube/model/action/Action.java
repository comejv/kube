package kube.model.action;

import java.io.Serializable;

import kube.model.Game;
import kube.model.Player;

public class Action implements Serializable {
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
    public static final int PRINT_NEXT_PLAYER = 17;
    public static final int PRINT_PLAYER = 18;
    public static final int PRINT_PLAYER_NAME = 19;
    public static final int PRINT_START = 23;
    public static final int PRINT_STATE = 24;
    public static final int PRINT_WELCOME = 31;
    public static final int PRINT_WIN_MESSAGE = 32;
    public static final int PRINT_FORBIDDEN_ACTION = 33;
    public static final int PRINT_ASK_NB_PLAYERS = 34;
    public static final int PRINT_ASK_PLAYER_NAME = 35;
    public static final int PRINT_ASK_GAME_MODE = 36;
    //NETWORK
    public static final int PRINT_ASK_HOST_OR_JOIN = 37;
    public static final int PRINT_ASK_IP = 38;
    public static final int INIT_K3 = 39;
    public static final int PLAYER_DATA = 40;
    public static final int PRINT_WAITING_FOR_CONNECTION = 41;
    public static final int PRINT_CONNECTION_ETABLISHED = 42;
    public static final int PRINT_CONNECTION_ERROR = 43;
    public static final int PRINT_NOT_YOUR_TURN = 44;
    public static final int ITS_YOUR_TURN = 45;
    public static final int MOVE_FROM_NETWORK = 46;


    private int type;
    private Object data;
    private int player;

    public Action(int type, Object data, int player){
        this.type = type;
        this.data = data;
        this.player = player;
    }

    public Action(int type, int player){
        this(type, null, player);
    }

    public Action(int type, Object data){
        this(type, data, Game.LOCAL);
    }

    public Action(int type){
        this(type, null,Game.LOCAL);
    }

    public void setType(int type){
        this.type = type;
    }

    public int getPlayer(){
        return player;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getType(){
        return type;
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString(){
        String s = "Action{type = " + type;
        if(data != null){
            s += ", data = " + data.toString();
        }
        s += ", id = " + getPlayer(); 
        s += "}";
        return s;
    }
        
}