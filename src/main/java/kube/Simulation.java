package kube;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.action.Action;
import kube.model.action.move.Move;
import kube.model.ai.MiniMaxAI;
import kube.model.ai.midLevelAI;
import kube.model.ai.randomAI;

public class Simulation implements Runnable {
    int winJ1;
    int winJ2;
    public static void main(String[] args) throws Exception {
        Simulation s = new Simulation();
        s.winJ1 = 0;
        s.winJ2 = 0;
        Thread t1 = new Thread(s);
        Thread t2 = new Thread(s);
        Thread t3 = new Thread(s);
        Thread t4 = new Thread(s);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        // Wait for each thread to finish
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        
        // Print winJ1 and winJ2 after all threads have finished
        System.out.println("Wins J1: " + s.winJ1);
        System.out.println("Wins J2: " + s.winJ2);
        System.out.println("Winrate J1: " + (double)s.winJ1 / (s.winJ1 + s.winJ2) * 100 + "%");
        System.out.println("Winrate J2: " + (double)s.winJ2 / (s.winJ1 + s.winJ2) * 100 + "%");
    }


    @Override
    public void run() {
        try {
            aiTrainingGames();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void upWinJ1(){
        winJ1++;
    }
    synchronized void upWinJ2(){
        winJ2++;
    }

    private void aiTrainingGames() throws Exception {
        Kube k = new Kube(true);
        int nGame = 5000;
        for (int i = 0; i < nGame; i++) {
            int seed = findSeedWithEquivalentDistributionToPlayers();
            k.init(new randomAI(0, seed), new randomAI(0, seed), seed);
            k.getCurrentPlayer().getAI().constructionPhase();
            k.updatePhase();
            k.getCurrentPlayer().getAI().constructionPhase();
            k.updatePhase();
            k.setCurrentPlayer(k.getP1());
            while (k.canCurrentPlayerPlay()) {
                Move move = k.getCurrentPlayer().getAI().nextMove();
                k.playMove(move);
            }
            if (k.getCurrentPlayer() == k.getP1()) {
                upWinJ2();
            } else {
                upWinJ1();
            }
            System.out.println("Partie n°" + i + " finie");
        }
    }

    private void testSeededRandom() {
        int seed = 180;
        Kube k = new Kube(true);
        Kube k2 = new Kube(true);
        k.init(new MiniMaxAI(0, 1), new MiniMaxAI(0, 1), seed);
        k2.init(new MiniMaxAI(0, 1), new MiniMaxAI(0, 1), seed);

        k.getCurrentPlayer().getAI().constructionPhase();
        k.updatePhase();
        k.getCurrentPlayer().getAI().constructionPhase();

        k2.getCurrentPlayer().getAI().constructionPhase();
        k2.updatePhase();
        k2.getCurrentPlayer().getAI().constructionPhase();

        System.out.println(k.getP1());
        System.out.println(k.getP2());
        System.out.println(k2.getP1());
        System.out.println(k2.getP2());
        System.out.println("Seed:" + seed + " " + k.getP1().getMountain().equals(k2.getP1().getMountain()));
    }

    private int findSeedWithEquivalentDistributionToPlayers() {
        int seed;
        Random r = new Random();
        while (true) {
            seed = r.nextInt();
            Kube k = new Kube();
            k.fillBag(seed);
            k.fillBase();
            k.distributeCubesToPlayers();
            if (k.getP1().getAvalaibleToBuild().equals(k.getP2().getAvalaibleToBuild())) {
                break;
            }
        }
        //System.out.println("Seed avec une même distribution de cube pour les joueurs :" + seed);
        return seed;

    }

    private void simulateNumberOfSlotAvailable() {
        float nTest = 1000;
        float availaibleSum = 0;
        for (int i = 0; i < nTest; i++) {
            Kube k = new Kube();
            Mountain m = k.getK3();
            k.fillBag();
            k.fillBase();
            availaibleSum += randomAdd(m);
        }
        System.out
                .println("Au total, sur " + (int) nTest + " simulations, il y a eu en moyenne " + availaibleSum / nTest
                        + " cases disponible à chaque coup");
    }

    private void simulateNumberOfCubeWithdrawable() {
        float nTest = 1000;
        float removableSum = 0;
        Mountain m = new Mountain(6);
        Kube k = new Kube();
        for (int i = 0; i < nTest; i++) {
            k.fillBag();
            randomFillMountain(m, k.getBag());
            removableSum += randomWithdraw(m);
        }
        System.out.println("Au total, sur " + (int) nTest + " simulations, il y a eu en moyenne " + removableSum / nTest
                + " pieces retirable à chaque coup");
    }

    // A method to fill a moutain with random colors + 2 white and 2 natural
    private void randomFillMountain(Mountain m, ArrayList<Color> bag) {
        Color c;
        int n_white = 2, n_natural = 2;
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                c = Color.getRandomColor();
                if (c == Color.NATURAL && n_natural > 0) {
                    n_natural--;
                    m.setCase(i, j, c);
                } else if (c == Color.WHITE && n_white > 0) {
                    n_white--;
                    m.setCase(i, j, c);
                } else {
                    m.setCase(i, j, bag.remove(0));
                }
            }
        }
    }

    private float randomWithdraw(Mountain m) {
        Random r = new Random();
        float removablesSum = 0;
        ArrayList<Point> removables = m.removable();
        while (removables.size() > 0) {
            removablesSum += removables.size();
            Point p = removables.remove(r.nextInt(removables.size()));
            m.setCase(p.x, p.y, Color.EMPTY);
            removables = m.removable();
        }
        return removablesSum / 21;

    }

    private float randomAdd(Mountain m) {
        Random r = new Random();
        int nMove = 0;
        float availableSum = 0;
        Color c = Color.NATURAL;
        ArrayList<Point> addable = m.compatible(c);
        while (addable.size() > 0) {
            availableSum += addable.size();
            Point p = addable.remove(r.nextInt(addable.size()));
            m.setCase(p.x, p.y, c);
            addable = m.compatible(c);
            nMove++;
        }
        return availableSum / nMove;

    }

}
