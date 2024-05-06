package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.model.Color;
import kube.model.Player;

public class utilsAI {
    public static void randomFillMoutain(Player player, Random r) {
        ArrayList<Color> colArr = new ArrayList<>(player.getAvalaibleToBuild().keySet());
        for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                int n = r.nextInt(colArr.size());
                Color c = colArr.get(n);
                player.addToMountain(i, j, c);
                if (!player.isAvailableToBuild(c)) {
                    colArr.remove(n);
                }
            }
        }
    }
}
