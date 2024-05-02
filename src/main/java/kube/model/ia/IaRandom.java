package kube.model.ia;


import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.Move;
import kube.model.Player;

public class IaRandom implements Ia {
    Kube k3;
    Player player;

    public IaRandom(Kube k, Player p){
        k3 = k;
        player = p;
    }

    public void preparationPhase() {
        Color c;
        for (int i = 0; i < player.getMountain().getBaseSize(); i++){
            for (int j = 0; j < i + 1; j++){
                c = player.getAvalaibleToBuild().get(0);
            }
        }
    }

    public void gamePhase() {
        throw new UnsupportedOperationException("Unimplemented method 'gamePhase'");
    }

    public Move nextMove() {
        throw new UnsupportedOperationException("Unimplemented method 'nextMove'");
    }
    
}
