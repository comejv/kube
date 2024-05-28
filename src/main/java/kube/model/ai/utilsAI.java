package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.model.ModelColor;
import kube.model.Player;

public class utilsAI {

    /**
     * Fill the mountain of the player with random colors
     * 
     * @param player
     * @param r
     */
    public static void randomFillMountain(Player player, Random r) {
        ArrayList<ModelColor> colArr = new ArrayList<>();
        // If the Mountain is already build
        if (player.getMountain().isFull()) {
            for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
                for (int j = 0; j < i + 1; j++) {
                    colArr.add(player.getMountain().getCase(i, j));
                    player.getMountain().setCase(i, j, ModelColor.EMPTY);
                }
            }
        }
        // If the Mountain is not build
        else {
            for (ModelColor color : player.getAvailableToBuild().keySet()) {
                int n = player.getAvailableToBuild().get(color);
                for (int i = 0; i < n; i++) {
                    colArr.add(color);
                }
                player.getAvailableToBuild().put(color, 0);
            }
        }
        for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                player.getMountain().setCase(i, j, colArr.remove(r.nextInt(colArr.size())));
            }
        }
    }
}
