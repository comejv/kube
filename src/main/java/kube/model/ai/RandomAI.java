package kube.model.ai;


import java.util.ArrayList;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.move.*;

public class RandomAI implements AI {
    Kube k3;
    Player player;

    public RandomAI(Kube k, Player p){
        k3 = k;
        player = p;
    }

    public void preparationPhase() {
        ArrayList<Color> colArr = new ArrayList<>(player.getAvalaibleToBuild().keySet());
    }

    public void gamePhase() {
        throw new UnsupportedOperationException("Unimplemented method 'gamePhase'");
    }

    public Move nextMove() {
        throw new UnsupportedOperationException("Unimplemented method 'nextMove'");
    }
    
}
