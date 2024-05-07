package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.model.Kube;
import kube.model.Player;
import kube.model.move.*;

public class RandomAI implements AI {
    Kube k3;
    Player iaPlayer;
    Random r;

    public RandomAI(Kube k, Player p, int seed) {
        k3 = k;
        iaPlayer = p;
        r = new Random(seed);
    }

    public RandomAI(Kube k, Player p) {
        k3 = k;
        iaPlayer = p;
        r = new Random();
    }

    public void preparationPhase() {
        utilsAI.randomFillMountain(iaPlayer, r);
    }

    public void gamePhase() {
        
    }

    public Move nextMove() throws Exception {
        ArrayList<Move> moves = k3.moveSet();
        if (moves.size() == 0){
            throw new Exception("Aucun coup jouable");
        } else {
            return moves.get(r.nextInt(moves.size()));
        }
    }

}
