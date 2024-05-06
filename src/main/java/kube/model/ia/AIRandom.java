package kube.model.ia;


import java.util.ArrayList;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.move.*;

public class AIRandom implements AI {
    Kube k3;
    Player player;

    public AIRandom(Kube k, Player p){
        k3 = k;
        player = p;
    }

    public void preparationPhase() {

    }

    public void gamePhase() {
        throw new UnsupportedOperationException("Unimplemented method 'gamePhase'");
    }

    public Move nextMove() {
        throw new UnsupportedOperationException("Unimplemented method 'nextMove'");
    }
    
}
