package kube.model;

import java.awt.Point;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import kube.model.action.move.Move;

public class History {

    /**********
     * ATTRIBUTES
     **********/

    @JsonProperty("first_player")
    private int firstPlayer;

    @JsonProperty("done")
    private ArrayList<Move> done;

    @JsonProperty("un_done")
    private ArrayList<Move> undone;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class History
     */
    @JsonCreator
    public History() {
        this.firstPlayer = 0;
        this.done = new ArrayList<>();
        this.undone = new ArrayList<>();
    }

    /**********
     * SETTERS
     **********/

    @JsonSetter("first_player")
    public void setFirstPlayer(int player) {
        this.firstPlayer = player;
    }

    @JsonSetter("done")
    public void setDone(ArrayList<Move> done) {
        this.done = done;
    }

    @JsonSetter("un_done")
    public void setUndone(ArrayList<Move> undone) {
        this.undone = undone;
    }

    /**********
     * GETTERS
     **********/

    @JsonGetter("first_player")
    public int getFirstPlayer() {
        return this.firstPlayer;
    }

    @JsonGetter("done")
    public ArrayList<Move> getDone() {
        return this.done;
    }

    @JsonGetter("un_done")
    public ArrayList<Move> getUndone() {
        return this.undone;
    }

    /**********
     * METHODS
     **********/

    /**
     * Add a move to the history
     *
     * @param move the move to add
     * @return void
     */
    public void addMove(Move move) {
        getDone().add(move);
        clearUndone();
    }

    /**
     * Pop the last move from the done list and add it to the undone list
     * 
     * @return the last move of the history
     */
    public Move undoMove() {
        Move move;
        move = getDone().get(getDone().size() - 1);
        getDone().remove(getDone().size() - 1);
        getUndone().add(move);
        return move;
    }

    /**
     * Pop the last move from the undone list and add it to the done list
     * 
     * @return the last move of the undone list
     */
    public Move redoMove() {
        Move move;
        move = getUndone().get(getUndone().size() - 1);
        getUndone().remove(getUndone().size() - 1);
        getDone().add(move);
        return move;
    }

    /**
     * Clear the history
     * 
     * @return void
     */
    public void clear() {
        clearDone();
        clearUndone();
    }

    /**
     * Clear the undone list
     * 
     * @return void
     */
    public void clearUndone() {
        getUndone().clear();
    }

    /**
     * Clear the done list
     * 
     * @return void
     */
    public void clearDone() {
        getDone().clear();
    }

    /**
     * Check if the history can be undone
     * 
     * @return true if the history can be undone, false otherwise
     */
    public boolean canUndo() {
        return getDone().size() > 0;
    }

    /**
     * Check if the history can be redone
     * 
     * @return true if the history can be redone, false otherwise
     */
    public boolean canRedo() {
        return getUndone().size() > 0;
    }

    /**
     * Return a string representing the history for displaying it
     * 
     * @param IA true if the history is displayed for the IA, false otherwise
     * @return a string representing the history
     */
    public String forDisplay(boolean IA) {
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
            s += (int) (position.getX() + 1) + ", " + (int) (position.getY() + 1) + ")<br>";
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
