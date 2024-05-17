package kube.model.ai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import kube.model.Kube;
import kube.model.action.move.Move;

public class midLevelAI extends MiniMaxAI {
    /**********
     * CONSTRUCTORS
     **********/

     public midLevelAI(int time, Random r) {
        setR(r);
        setTime(time);
    }

    public midLevelAI(int time, int seed) {
        setR(new Random(seed));
        setTime(time);
    }

    public midLevelAI(int time) {
        setR(new Random());
        setTime(time);
    }

    public midLevelAI() {
        setR(new Random());
        setTime(1000);
    }

    @Override
    public int evaluation(Kube k) {
        return getPlayer(k).getPlayableColors().size() + getPlayer(k).getAdditionals().size();
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap) {
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }
}
