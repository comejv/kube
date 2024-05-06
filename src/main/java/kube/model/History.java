package kube.model;

import java.util.ArrayList;

import kube.model.move.Move;

import java.awt.Point;

public class History {
    // Attributes
    private int firstPlayer;
    private ArrayList<Move> done;
    private ArrayList<Move> undone;

    // Constructor
    public History() {
        setFirstPlayer(0);
        setDone(new ArrayList<Move>());
        setUndone(new ArrayList<Move>());
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
        getDone().add(move);
        clearUndone();
    }

    public Move undoMove() {
        Move move = getDone().get(getDone().size() - 1);
        getDone().remove(this.done.size() - 1);
        getUndone().add(move);
        return move;
    }

    public Move redoMove() {
        Move move = getUndone().get(getUndone().size() - 1);
        getUndone().remove(getUndone().size() - 1);
        getDone().add(move);
        return move;
    }


    public void clear() {
        getDone().clear();
        clearUndone();
    }

    public void clearUndone() {
        getUndone().clear();
    }

    public void clearDone() {
        getDone().clear();
    }

    public boolean canUndo() {
        return getDone().size() > 0;
    }

    public boolean canRedo() {
        return getUndone().size() > 0;
    }

    public String forSave() {
        String s = "";
        s += getFirstPlayer() + "\n{";
        for (Move move : getDone()) {
            s += move.forSave() + ";";
        }
        if(canUndo()){
            s = s.substring(0, s.length() - 1);
        }
        s += "}\n{";
        for (Move move : getUndone()) {
            s += move.forSave() + ";";
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
            Point position = m.getFrom();
            if (IA) {
                if (player.getId() == 1) {
                    s += "You" + " : (";
                } else {
                    s += "AI" + " : (";
                }
            } else {
                s += "P" + player.getId() + " : (";
            }
            s += (int) (position.getX() + 1) + ", " + (int) (position.getY() + 1 )+ ")<br>";
        }
        s += "</html>";
        return s;
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
