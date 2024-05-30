package kube.model.ai;

// Import model classes
import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;

// Import java classes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import javax.swing.Timer;

public abstract class MiniMaxAI implements ActionListener, Serializable {

    /**********
     * ATTRIBUTES
     **********/

    private int iaPlayerId, nbMoves, horizonMax, time;
    private boolean noMoreTime;
    private Random random;
    private Timer timer;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the MiniMaxAI class
     * 
     * @param time   the time of the timer
     * @param random the random object
     */
    public MiniMaxAI(int time, Random random) {
        this.random = random;
        this.time = time;
    }

    /**
     * Constructor of the MiniMaxAI class
     * 
     * @param time the time of the timer
     * @param seed the seed of the random object
     */
    public MiniMaxAI(int time, int seed) {
        this.random = new Random(seed);
        this.time = time;
    }

    /**
     * Constructor of the MiniMaxAI class totally random
     * 
     * @param time the time of the timer
     */
    public MiniMaxAI(int time) {
        this.random = new Random();
        this.time = time;
    }

    /**
     * Constructor of the MiniMaxAI class with default time (1s) and random object
     */
    public MiniMaxAI() {
        this.random = new Random();
        this.time = 1000;
    }

    /**********
     * SETTERS
     **********/

    public final void setPlayerId(int id) {
        iaPlayerId = id;
    }

    public final void setNoMoreTime(boolean b) {
        noMoreTime = b;
    }

    public final void setHorizonMax(int horizon) {
        horizonMax = horizon;
    }

    public final void setNbMoves(int nb) {
        nbMoves = nb;
    }

    public final void setTimer(Timer t) {
        timer = t;
    }

    /**********
     * GETTERS
     **********/

    public Random getRandom() {
        return random;
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

    public int getPlayerId() {
        return iaPlayerId;
    }

    public int getNbMoves() {
        return nbMoves;
    }

    public int getHorizonMax() {
        return horizonMax;
    }

    /**
     * Getting the player
     * 
     * @param kube the kube
     * @return the player
     */
    public Player getPlayer(Kube kube) {
        return kube.getPlayerById(getPlayerId());
    }

    /**
     * Getting the other player
     * 
     * @param kube the kube
     * @return the other player
     */
    public Player getOtherPlayer(Kube kube) {
        if (kube.getP1().getId() == getPlayerId()) {
            return kube.getP2();
        }
        return kube.getP1();
    }

    /**********
     * METHODS
     **********/

    /**
     * Increment the number of moves
     * 
     * @return void
     */
    public void incrNbMoves() {
        setNbMoves(getNbMoves() + 1);
    }

    /**
     * Fill the mountain with random cubes
     * 
     * @return void
     */
    public void constructionPhase(Kube k3) {
        utilsAI.randomFillMountain(getPlayer(k3), getRandom());
    }

    /**
     * MinMax algorithm
     * 
     * @param kube    the current state of the game
     * @param horizon the depth of the tree
     * @return an hashmap of each move doable and it score
     */
    public HashMap<Move, Integer> miniMax(Kube kube, int horizon) {

        HashMap<Move, Integer> map;
        ArrayList<Move> moves;
        Integer minValue, maxValue;

        map = new HashMap<>();
        moves = kube.moveSet();

        // Loop until timers end
        for (Move m : moves) {

            if (getNoMoreTime()) {
                return null;
            }

            kube.playMove(m);
            minValue = Integer.MIN_VALUE;
            maxValue = Integer.MAX_VALUE;
            map.put(m, miniMaxRec(kube.clone(), horizon, minValue, maxValue));
            kube.unPlay();
        }
        return map;
    }

    /**
     * MinMax recursive algorithm
     * 
     * @param kube    the current state of the game
     * @param horizon the depth of the tree
     * @param alpha   the best value for a max node
     * @param beta    the worst value for a min node
     * @return the best value for a max node or the worst value for a min node
     */
    public int miniMaxRec(Kube kube, int horizon, int alpha, int beta) {

        ArrayList<Move> moves;
        int bestScore, score, evalValue, evalOtherPlayerValue;
        Player player;
        boolean hasNotPenlaity, hasNotMoves;

        // Check if the timer is ended
        if (getNoMoreTime()) {
            return -1;
        }

        player = kube.getCurrentPlayer();

        // Avoid evaluation of penality moves, because that false the game state
        hasNotPenlaity = !kube.getPenality() && horizon <= 0;
        hasNotMoves = (moves = kube.moveSet()).size() == 0;
        if (hasNotPenlaity || hasNotMoves) {
            evalValue = evaluation(kube, getPlayer(kube));
            evalOtherPlayerValue = evaluation(kube, getOtherPlayer(kube));
            return evalValue - evalOtherPlayerValue;
        } else {
            if (player == getPlayer(kube)) {
                bestScore = Integer.MIN_VALUE;
            } else {
                bestScore = Integer.MAX_VALUE;
            }
            for (Move m : moves) {

                kube.playMove(m);
                score = miniMaxRec(kube, horizon - 1, alpha, beta);

                // Check if the timer is ended
                if (getNoMoreTime()) {
                    return -1;
                }
                kube.unPlay();

                // Max node
                if (player == getPlayer(kube)) {
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(score, alpha);
                    if (beta <= alpha) {
                        break;
                    }
                }
                // Min node
                else {
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(score, beta);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestScore;
        }
    }

    /**
     * Kube's evaluation method
     * 
     * @param kube   the kube
     * @param player the player
     * @return the value of the state
     */
    abstract public int evaluation(Kube kube, Player player);

    /**
     * Action performed when the timer ends
     * 
     * @param action the action event
     * @return void
     */
    @Override
    public void actionPerformed(ActionEvent action) {
        setNoMoreTime(true);
        getTimer().stop();
    }

    /**
     * Select the best move
     * 
     * @param movesMap the moves map
     * @param kube     the kube
     * @return the best move
     */
    public Move selectMove(HashMap<Move, Integer> movesMap, Kube kube) {
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    /**
     * Give the next move
     * 
     * @param kube the kube
     * @return the next move
     */
    public Move nextMove(Kube kube) {

        int horizon;
        HashMap<Move, Integer> solution, moveMap;

        incrNbMoves();
        setNoMoreTime(false);
        setTimer(new Timer(time, this));
        getTimer().start();

        horizon = 2;
        solution = null;
        moveMap = null;

        while (true) {
            moveMap = miniMax(kube.clone(), horizon);
            if (getNoMoreTime()) {
                if (solution == null) {
                    return selectMove(moveMap, kube);
                } else {
                    return selectMove(moveMap, kube);
                }
            } else {
                solution = moveMap;
                setHorizonMax(horizon);
                horizon++;
            }
        }
    }

    /**
     * Clone the MiniMaxAI object
     * 
     * @return the cloned object
     */
    public MiniMaxAI clone() {
        // TODO: export in each AI classes
        if (this instanceof moveSetHeuristique) {
            return new moveSetHeuristique(getTime());
        } else if (this instanceof randomAI) {
            return new randomAI(getTime());
        } else {
            throw new UnsupportedOperationException("Unsupported type for cloning.");
        }
    }
}
