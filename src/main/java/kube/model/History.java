package kube.model;

import java.util.ArrayList;
import java.awt.Point;

import kube.model.Move;

public class History {
    // Attributes
    private int firstPlayer;
    private ArrayList<Move> done;
    private ArrayList<Move> undone;

    // Constructor
    public History(int player) {
        setFirstPlayer(player);
        this.done = new ArrayList<Move>();
        this.undone = new ArrayList<Move>();
    }

    // Getters
    public int getFirstPlayer() {
        return this.firstPlayer;
    }

    public ArrayList<Move> getDone() {
        return this.done;
    }

    public ArrayList<Move> getUndone() {
        return this.undone;
    }

    // Setters
    public void setFirstPlayer(int player) {
        this.firstPlayer = player;
    }

    public void setDone(ArrayList<Move> done) {
        this.done = done;
    }

    public void setUndone(ArrayList<Move> undone) {
        this.undone = undone;
    }

    // Methods
    public void addMove(Move move) {
        this.done.add(move);
        clearUndone();
    }

    public void undoMove() {
        Move move = this.done.get(this.done.size() - 1);
        this.done.remove(this.done.size() - 1);
        this.undone.add(move);
    }

    public void redoMove() {
        Move move = this.undone.get(this.undone.size() - 1);
        this.undone.remove(this.undone.size() - 1);
        this.done.add(move);
    }

    public void clearUndone() {
        this.undone.clear();
    }

    public void clear() {
        this.done.clear();
        this.undone.clear();
    }

    public boolean canUndo() {
        return this.done.size() > 0;
    }

    public boolean canRedo() {
        return this.undone.size() > 0;
    }

    public String forSave() {
        String s = "";
        s += this.firstPlayer + "\n {";
        for (Move move : this.done) {
            s += move.forSave() + ",";
        }
        if(canUndo()){
            s = s.substring(0, s.length() - 1);
        }
        s += "}\n {";
        for (Move move : this.undone) {
            s += move.forSave() + ",";
        }
        if(canRedo()){
            s = s.substring(0, s.length() - 1);
        }
        s += "}";

        return s;
    }


    public String forDisplay(boolean IA){
        String s = "<html>";
        for (int i = done.size() - 1; i >= 0; i--) {
            Move m = done.get(i);
            Player player = m.getPlayer();
            Point position = m.getPosition();
            if (IA) {
                if (player.getNum() == 1) {
                    s += "You" + " : (";
                } else {
                    s += "AI" + " : (";
                }
            } else {
                s += "P" + player.getNum() + " : (";
            }
            s += (int) (position.getX() + 1) + ", " + (int) (position.getY() + 1 )+ ")<br>";
        }
        s += "</html>";
        return s;
    }
    }


    @Override
    public String toString() {
        String s = "History : \n";
        if (canUndo()) {
            s += "Done :  { \n";
            for (Move move : done) {
                s += move.toString() + "\n";
            }
            s += " } \n";
        }
        if (canRedo()) {
            s += "Undone :  { \n";
            for (Move move : undone) {
                s += move.toString() + "\n";
            }
            s += " } \n";
        }

        return s;
    }

}
