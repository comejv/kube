package kube.model;

import java.util.ArrayList;

import kube.model.action.move.Move;

import java.awt.Point;

public class History {

    /**********
     * ATTRIBUTES
     **********/

    private int firstPlayer;
    private ArrayList<Move> done;
    private ArrayList<Move> undone;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class History
     */
    public History() {
        setFirstPlayer(0);
        setDone(new ArrayList<Move>());
        setUndone(new ArrayList<Move>());
    }

    /**
     * Constructor of the class History from a save string
     * 
     * @param save the string to load
     */
    public History(String save) {

        String player, doneString, undoneString;
        String[] parts, done, undone;

        parts = save.split("\n");

        player = parts[0];
        setFirstPlayer(Integer.parseInt(player));

        doneString = parts[1].substring(1, parts[1].length() - 1);
        done = doneString.split(" ");
        setDone(new ArrayList<Move>());
        for (String move : done) {
            getDone().add(Move.fromSave(move));
        }

        undoneString = parts[2].substring(1, parts[2].length() - 1);
        undone = undoneString.split(" ");
        setUndone(new ArrayList<Move>());
        for (String move : undone) {
            getUndone().add(Move.fromSave(move));
        }
    }
    /**********
     * SETTERS
     **********/

    public void setFirstPlayer(int player) {
        this.firstPlayer = player;
    }

    public void setDone(ArrayList<Move> done) {
        this.done = done;
    }

    public void setUndone(ArrayList<Move> undone) {
        this.undone = undone;
    }

    /**********
     * GETTERS
     **********/

    public int getFirstPlayer() {
        return this.firstPlayer;
    }

    public ArrayList<Move> getDone() {
        return this.done;
    }

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
     * Return a string representing the history for saving it
     * 
     * @return a string representing the history
     */
    public String forSave() {

        String save;
        // Saving the first player
        save = getFirstPlayer() + "\n[";
        // Saving the done moves
        for (Move move : getDone()) {
            save += move.forSave() + " ";
        }
        if (canUndo()) {
            save = save.substring(0, save.length() - 1);
        }
        // Saving the undone moves
        save += "]\n[";
        for (Move move : getUndone()) {
            save += move.forSave() + " ";
        }
        if (canRedo()) {
            save = save.substring(0, save.length() - 1);
        }

        save += "]";
        return save;
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
