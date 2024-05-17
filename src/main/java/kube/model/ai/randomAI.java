package kube.model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kube.model.Kube;
import kube.model.action.move.Move;

public class randomAI extends MiniMaxAI {
    public randomAI(int time, Random r) {
        setR(r);
        setTime(time);
    }

    public randomAI(int time, int seed) {
        setR(new Random(seed));
        setTime(time);
    }

    public randomAI(int time) {
        setR(new Random());
        setTime(time);
    }

    public randomAI() {
        setR(new Random());
        setTime(1000);
    }

    @Override
    public int evaluation(Kube k) {
        return 0;
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap) {
        ArrayList<Move> moves = getK3().moveSet();
        return moves.get(new Random().nextInt(moves.size()));
    }

    @Override
    public void constructionPhase() {
        while (getPlayer(getK3()).getPlayableColors().size() == 0 && !getPlayer(getK3()).validateBuilding()) {
            utilsAI.randomFillMountain(getPlayer(getK3()), getR());
        }
    }
}
