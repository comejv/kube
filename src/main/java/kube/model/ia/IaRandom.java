package kube.model.ia;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.move.*;

public class IaRandom implements Ia {
    Kube k3;
    Player player;
    Random r;

    public IaRandom(Kube k, Player p) {
        k3 = k;
        player = p;
        r = new Random();
    }

    public void preparationPhase() {
        ArrayList<Color> colArr = new ArrayList<>(player.getAvalaibleToBuild().keySet());
        for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                int n = r.nextInt(colArr.size());
                player.addToMountain(i,j, colArr.get(n));
                if (!player.isAvailableToBuild(colArr.get(n))){
                    colArr.remove(n);
                }
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
