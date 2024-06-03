package kube.model;

// Import model classes
import kube.model.action.move.Move;

// Import java classes
import java.awt.Point;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class History implements Serializable {

    // TODO : refactor this class to make it more readable
    
    /**********
     * ATTRIBUTES
     **********/

    private int firstPlayer;
    private CopyOnWriteArrayList<Move> done;
    private CopyOnWriteArrayList<Move> undone;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class History
     */
    public History() {
        this.firstPlayer = 0;
        this.done = new CopyOnWriteArrayList<>();
        this.undone = new CopyOnWriteArrayList<>();
    }

    /**********
     * SETTERS
     **********/

    public final void setDone(CopyOnWriteArrayList<Move> done) {
        this.done = done;
    }

    public final void setUndone(CopyOnWriteArrayList<Move> undone) {
        this.undone = undone;
    }

    /**********
     * GETTERS
     **********/

    public int getFirstPlayer() {
        return this.firstPlayer;
    }

    public CopyOnWriteArrayList<Move> getDone() {
        return this.done;
    }

    public CopyOnWriteArrayList<Move> getUndone() {
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
    public String forDisplay() {
        String s = "<html>";
        for (int i = done.size() - 1; i >= 0; i--) {
            Move m = done.get(i);
            Player player = m.getPlayer();
            Point position = m.getFrom();
            s += player.getName() + " : (";
            s += (int) (position.getX() + 1) + ", " + (int) (position.getY() + 1) + ")<br>";
        }
        s += "</html>";
        return s;
    }

    /**********
     * OVERRIDES
     **********/

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
