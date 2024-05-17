package kube.model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kube.model.action.move.Move;

public class RandomAI extends MiniMaxAI {
    public RandomAI(int time, Random r) {
        setR(r);
        setTime(time);
    }

    public RandomAI(int time, int seed) {
        setR(new Random(seed));
        setTime(time);
    }

    public RandomAI(int time) {
        setR(new Random());
        setTime(time);
    }

    public RandomAI() {
        setR(new Random());
        setTime(1000);
    }
    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap) {
        ArrayList<Move> moves = getK3().moveSet();
        return moves.get(new Random().nextInt(moves.size()));
    }
}
