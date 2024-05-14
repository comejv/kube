package kube.model;

import kube.model.ai.*;
import kube.configuration.Config;
import kube.model.action.*;

public class Game implements Runnable {
    public static final int localTextual = 1;
    public static final int onlineServerTextual = 2;
    public static final int onlineClientTextual = 3;

    Queue<Action> events;
    private int gameType;
    private Kube k3;

    public Game(int gameType, Kube k3) {
        this.gameType = gameType;
        this.k3 = k3;
    }

    @Override
    public void run() {
        switch (gameType) {
            case localTextual:
                localTextualGame();
                break;
            default:
                break;
        }
    }

    public void localTextualGame() {
        k3.init();
        k3.distributeCubesToPlayers();
        Player currentPlayerToBuild = k3.getP1();
        while (k3.getPhase() == 1) {
            if (currentPlayerToBuild.isAI()){
                currentPlayerToBuild.getAI().constructionPhase();
            } else {
                Action a = events.remove();
                Config.debug(a);
            }
        }
    }
}
