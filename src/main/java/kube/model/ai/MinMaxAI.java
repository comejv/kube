package kube.model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kube.model.Kube;
import kube.model.Player;
import kube.model.move.Move;

public class MinMaxAI implements AI {
    Kube k3;
    Player iaPlayer;
    Random r;
    HashMap<Kube, Move> solution;
    int maxHorizon;

    public MinMaxAI(Kube k, Player p, int seed) {
        this(k, p);
        r = new Random(seed);
    }

    public MinMaxAI(Kube k, Player p) {
        k3 = k;
        iaPlayer = p;
        r = new Random();
        solution = new HashMap<>();
        maxHorizon = 10;
    }

    @Override
    public void preparationPhase() {
        utilsAI.randomFillMoutain(iaPlayer, r);
    }

    @Override
    public void gamePhase() {
        //minMax(k3.clone(), maxHorizon);
    }

    @Override
    public Move nextMove() throws Exception {
        if (solution.containsKey(k3)){
            return solution.get(k3);
        } else {
            //minMax(k3.clone(), maxHorizon);
            return nextMove();
        }
    }

    public int minMax(Kube k, int horizon) {
        ArrayList<Move> moves = k.moveSet();
        // Leaf
        if (horizon == 0 || moves.size() == 0) {
            return evaluation(k);
        } else {
            int value, score;
            if (k.getCurrentPlayer() == iaPlayer) {
                value = Integer.MIN_VALUE;
            } else {
                value = Integer.MAX_VALUE;
            }
            for (Move m : moves){
                k.playMove(m);
                score = minMax(k, horizon - 1);
                k.unPlay();
                if (k.getCurrentPlayer() == iaPlayer) {
                    if (score > value){
                        value = score;
                        //solution.put(k.clone(), m);
                    }
                } else {
                    if (score < value){
                        value = score;
                    }
                }
            }
            return value;
        }
    }

    private int evaluation(Kube k) {
        return k.getCurrentPlayer().getPlayableColors().size();
    }
}
