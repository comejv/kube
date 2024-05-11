package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.configuration.Config;
import kube.model.Color;
import kube.model.Player;

public class utilsAI {
    public static void randomFillMountain(Player player, Random r) {

        ArrayList<Color> colArr= new ArrayList<>();
        //If the Mountain is already build 
        if (player.getMountain().isFull()){
            for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
                for (int j = 0; j < i + 1; j++) {
                    colArr.add(player.getMountain().getCase(i, j));
                    player.getMountain().setCase(i, j, Color.EMPTY);
                }
            }
        }
        //If the Mountain is not build
        else {
            for (Color color : player.getAvalaibleToBuild().keySet()) {
                int n = player.getAvalaibleToBuild().get(color);
                for (int i = 0; i < n; i++) {
                    colArr.add(color);
                }
                player.getAvalaibleToBuild().put(color, 0);
            }
        }
        for (int i = 0; i < player.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                player.getMountain().setCase(i, j, colArr.remove(r.nextInt(colArr.size())));
                }
            }
        }

    }
