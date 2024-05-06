package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Player;
import kube.model.move.*;

public class RandomAI implements AI {
    Kube k3;
    Player player;
    Random r;

    public RandomAI(Kube k, Player p) {
        k3 = k;
        player = p;
        r = new Random();
    }

    public void preparationPhase() {
        ArrayList<Color> colArr = new ArrayList<>(player.getAvalaibleToBuild().keySet());
        for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                int n = r.nextInt(colArr.size());
                Color c =  colArr.get(n);
                player.addToMountain(i, j, c);
                if (!player.isAvailableToBuild(c)){
                    colArr.remove(n);
                }
            }
        }
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
