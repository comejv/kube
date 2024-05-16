package kube.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kube.configuration.Config;
import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;

public class abstractMiniMax implements abstractAI, ActionListener {

    /**********
     * ATTRIBUTES
     **********/

    private Kube k3;
    private int iaPlayerId;
    private Random r;
    private int time; // in ms
    private boolean noMoreTime;
    private Timer timer;

    /**********
     * CONSTRUCTORS
     **********/

    public abstractMiniMax(int time, int seed) {
        r = new Random(seed);
        setTime(time);
    }

    public abstractMiniMax() {
        r = new Random();
        setTime(1000);
    }

    /**********
     * SETTERS
     **********/

    public void setK3(Kube k) {
        k3 = k;
    }

    public void setPlayerId(Player p) {
        iaPlayerId = p.getId();
    }

    public void setR(Random r) {
        this.r = r;
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

    public Player getPlayer(Kube k) {
        return k.getPlayerById(iaPlayerId);
    }

    public Random getR() {
        return r;
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
        while (!getPlayer(k3).validateBuilding()) {
            utilsAI.randomFillMountain(getPlayer(k3), getR());
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
        setNoMoreTime(false);
        setTimer(new Timer(time, this));
        getTimer().start();
        horizon = 2;
        HashMap<Move, Integer> solution = null, moveMap = null;
        while (true) {
            moveMap = miniMax(getK3().clone(), horizon);
            if (getNoMoreTime()) {
                Config.debug("Horizon max :" + horizon);
                if (solution == null) {
                    return selectMove(moveMap);
                } else {
                    return selectMove(solution);
                }
            } else {
                solution = moveMap;
                horizon++;
            }
        }
    }

    /**
     * MinMax algorithm
     * 
     * @param k       the current state of the game
     * @param horizon the depth of the tree
     * @return an hashmap of each move doable and it score
     */
    public HashMap<Move, Integer> miniMax(Kube k, int horizon) {
        HashMap<Move, Integer> map = new HashMap<>();
        ArrayList<Move> moves = k.moveSet();
        for (Move m : moves) {
            if (getNoMoreTime()) {
                return null;
            }
            k.playMove(m);
            map.put(m, miniMaxRec(k.clone(), horizon));
            k.unPlay();

        }
        return map;
    }

    /**
     * MinMax algorithm
     * 
     * @param k       the current state of the game
     * @param horizon the depth of the tree
     * @return the best value for a max node or the worst value for a min node
     */
    public int miniMaxRec(Kube k, int horizon) {

        // Timer's end
        if (getNoMoreTime()) {
            return -1;
        }

        ArrayList<Move> moves;
        int bestScore, score;
        Player p = k.getCurrentPlayer();
        // Leaf
        if (horizon == 0 || (moves = k.moveSet()).size() == 0) {
            return evaluation(k);
        } else {
            if (p == getPlayer(k)) {
                bestScore = Integer.MIN_VALUE;
            } else {
                bestScore = Integer.MAX_VALUE;
            }
            for (Move m : moves) {
                k.playMove(m);
                score = miniMaxRec(k, horizon - 1);
                if (getNoMoreTime()) { // Timer's end
                    return -1;
                }
                k.unPlay();
                if (p == getPlayer(k)) {
                    if (score > bestScore) {
                        bestScore = score;
                    }
                } else {
                    if (score < bestScore) {
                        bestScore = score;
                    }
                }
            }
            return bestScore;
        }
    }

    /**
     * Kube's evaluation method
     * 
     * @param k the current state of the game
     * @return the value of the state
     */
    private int evaluation(Kube k) {
        return getPlayer(k).getPlayableColors().size() + getPlayer(k).getAdditionals().size();
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

    public int moveSetSize() {
        return k3.moveSet().size();
    }

    public Move selectMove(HashMap<Move, Integer> movesMap) {
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }
}
