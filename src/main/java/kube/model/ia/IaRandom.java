package kube.model.ia;


import java.util.ArrayList;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.Move;
import kube.model.MoveAM;
import kube.model.MoveAW;
import kube.model.MoveMW;
import kube.model.Player;

public class IaRandom implements Ia {
    Kube k3;
    Player player;

    public IaRandom(Kube k, Player p){
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
