package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.*;

public class RandomAI implements abstractAI {
    Kube k3;
    Player iaPlayer;
    Random r;

    public RandomAI(int seed) {
        r = new Random(seed);
    }

    public RandomAI() {
        r = new Random();
    }

    public void constructionPhase() {
        utilsAI.randomFillMountain(iaPlayer, r);
    }

    public Move nextMove() throws Exception {
        ArrayList<Move> moves = k3.moveSet();
        if (moves.size() == 0){
            throw new Exception("Aucun coup jouable");
        } else {
            return moves.get(r.nextInt(moves.size()));
        }
    }
    
    public void setK3(Kube k){
        k3 = k;
    }

    public void setPlayer(Player p){
        iaPlayer = p;
    }
}
