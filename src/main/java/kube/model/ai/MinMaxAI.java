package kube.model.ai;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;

public class MinMaxAI implements abstractAI, ActionListener {

    /**********
     * ATTRIBUTES
     **********/

    private Kube k3;
    private Player iaPlayer;
    private Random r;
    private HashMap<Kube, Move> solution, tmp;
    private Method heuristic;
    private int time; // in ms
    private boolean noMoreTime;
    private Timer timer;

    /**********
     * CONSTRUCTORS
     **********/

    public MinMaxAI(int time, int seed) {
        r = new Random(seed);
        setTime(time);
    }

    public MinMaxAI() {
        r = new Random();
        setTime(3000);
    }

    /**********
     * SETTERS
     **********/

    public void setK3(Kube k) {
        k3 = k;
    }

    public void setPlayer(Player p) {
        iaPlayer = p;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public void setSolution(HashMap<Kube, Move> s) {
        solution = s;
    }

    public void setTmp(HashMap<Kube, Move> t) {
        tmp = t;
    }

    public void setHeuristic(Method m) {
        heuristic = m;
    }

    public void setTime(int t) {
        time = t;
    }

    public void setNoMoreTime(boolean b) {
        noMoreTime = b;
    }

    public void setTimer(Timer t) {
        timer = t;
    }

    /**********
     * GETTERS
     **********/

    public Kube getK3() {
        return k3;
    }

    public Player getPlayer() {
        return iaPlayer;
    }

    public Random getR() {
        return r;
    }

    public HashMap<Kube, Move> getSolution() {
        return solution;
    }

    public HashMap<Kube, Move> getTmp() {
        return tmp;
    }

    public Method getHeuristic() {
        return heuristic;
    }

    public int getTime() {
        return time;
    }

    public boolean getNoMoreTime() {
        return noMoreTime;
    }

    public Timer getTimer() {
        return timer;
    }

    /**********
     * METHODS
     **********/

    /**
     * Fill the mountain with random mountains until the mountain is valid
     * 
     * @return void
     */
    @Override
    public void constructionPhase() {
        while (!getPlayer().validateBuilding()) {
            utilsAI.randomFillMountain(getPlayer(), getR());
        }
    }

    /**
     * Give the next move
     * 
     * @return the next move
     */
    @Override
    public Move nextMove() throws Exception {

        int horizon;

        setSolution(new HashMap<>());
        setNoMoreTime(false);

        horizon = 1;

        setTimer(new Timer(time, this));
        getTimer().start();

        while (true) {
            setTmp(new HashMap<>());
            minMax(getK3().clone(), horizon);
            if (getNoMoreTime()) {
                return getSolution().get(getK3());
            } else {
                setSolution(getTmp());
                horizon++;
            }
        }
    }

    /**
     * MinMax algorithm
     * 
     * @param k       the current state of the game
     * @param horizon the depth of the tree
     * @return the best value for a max node or the worst value for a min node
     */
    public int minMax(Kube k, int horizon) {

        // Timer's end
        if (getNoMoreTime()) {
            return -1;
        }

        ArrayList<Move> moves;
        int value, score;

        // Leaf
        if (horizon == 0) {
            return evaluation(k);
        } else if ((moves = k.moveSet()).size() == 0) {
            return evaluation(k);
        } else {
            if (k.getCurrentPlayer() == getPlayer()) {
                value = Integer.MIN_VALUE;
            } else {
                value = Integer.MAX_VALUE;
            }
            for (Move m : moves) {
                k.playMove(m);
                score = minMax(k, horizon - 1);
                if (score == -1) { // Timer's end
                    return -1;
                }
                k.unPlay();
                if (k.getCurrentPlayer() == getPlayer()) {
                    if (score > value) {
                        value = score;
                        getTmp().put(k.clone(), m);
                    }
                } else {
                    if (score < value) {
                        value = score;
                    }
                }
            }
            return value;
        }
    }

    /**
     * Kube's evaluation method
     * 
     * @param k the current state of the game
     * @return the value of the state
     */
    private int evaluation(Kube k) {
        try {
            return (int) heuristic.invoke(null, k); // Assuming heuristic method is static
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // or handle the exception as appropriate
        }
    }

    @Override
    /**
     * Action performed when the timer ends
     * 
     * @param arg0 the action event
     * @return void
     */
    public void actionPerformed(ActionEvent arg0) {
        setNoMoreTime(true);
        getTimer().stop();
    }
}
