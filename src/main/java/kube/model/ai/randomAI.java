package kube.model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;

public class randomAI extends MiniMaxAI {

    // TODO: refactor

    public randomAI(int time, Random r) {
        super(time, r);
    }

    public randomAI(int time, int seed) {
        super(time, seed);

    }

    public randomAI(int time) {
        super(time);
    }

    public randomAI() {
        super();
    }

    @Override
    public int evaluation(Kube k, Player p) {
        return 0;
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap, Kube k3) {
        ArrayList<Move> moves = k3.moveSet();
        return moves.get(new Random().nextInt(moves.size()));
    }

    @Override
    public void constructionPhase(Kube k3) {
        while (getPlayer(k3).getPlayableColors().isEmpty()) {
            utilsAI.randomFillMountain(getPlayer(k3), getRandom());
        }
    }
}
