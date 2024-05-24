package kube;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kube.model.AI;
import kube.model.History;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.action.move.Move;
import kube.model.ai.MiniMaxAI;
import kube.model.ai.moveSetHeuristique;

public class Simulation implements Runnable {
    int winJ1;
    int winJ2;
    int nbMoveJ1;
    int nbMoveJ2;
    int nbGames;
    int nGamesFinished;
    int sumHorizonJ1;
    int sumHorizonJ2;

    public static void main(String[] args) throws Exception {
        int nbGames = 100;
        try{
            nbGames = Integer.parseInt(args[0]);
        }
        catch (Exception e){
            System.out.println("Nombre de parties par défaut: 100");
        }
        int nbThreads = 8;
        Simulation s = new Simulation(nbGames);
        s.winJ1 = 0;
        s.winJ2 = 0;
        s.nbMoveJ1 = 0;
        s.nbMoveJ2 = 0;
        Thread[] threads = new Thread[nbThreads];
        for (int i = 0; i < nbThreads; i++) {
            threads[i] = new Thread(s);
        }
        for (int i = 0; i < nbThreads; i++) {
            threads[i].start();
        }
        for (int i = 0; i < nbThreads; i++) {
            threads[i].join();
        }

        // Print winJ1 and winJ2 after all threads have finished
        System.out.println("\nNombre de parties: " + s.nbGames);
        System.out.println("Wins J1: " + s.winJ1);
        System.out.println("Wins J2: " + s.winJ2);
        System.out.println("Winrate J1: " + (double) s.winJ1 / (s.winJ1 + s.winJ2) * 100 + "%");
        System.out.println("Winrate J2: " + (double) s.winJ2 / (s.winJ1 + s.winJ2) * 100 + "%");
        System.out.println("Nombre de coup moyen du J1: " + (double) s.nbMoveJ1 / s.nbGames);
        System.out.println("Nombre de coup moyen du J2: " + (double) s.nbMoveJ2 / s.nbGames);
        System.out.println("Horizon moyen J1: " + (double) s.sumHorizonJ1 / s.nbMoveJ1);
        System.out.println("Horizon moyen J2: " + (double) s.sumHorizonJ1 / s.nbMoveJ2);

    }

    public Simulation(int nbGames) {
        this.nbGames = nbGames;
    }

    @Override
    public void run() {
        try {
            testMountain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void upWinJ1() {
        winJ1++;
    }

    synchronized void upWinJ2() {
        winJ2++;
    }

    synchronized void addNbMovesJ1(int n) {
        nbMoveJ1 += n;
    }

    synchronized void addNbMovesJ2(int n) {
        nbMoveJ2 += n;
    }

    synchronized void addToSumHorizonJ1(int h) {
        sumHorizonJ1 += h;
    }

    synchronized void addToSumHorizonJ2(int h) {
        sumHorizonJ2 += h;
    }

    public int getnGamesFinished() {
        return nGamesFinished;
    }

    synchronized void incrnGamesFinished() {
        nGamesFinished++;
    }

    public int getNbGames() {
        return nbGames;
    }

    public int getSumHorizonJ1() {
        return sumHorizonJ1;
    }

    public int getSumHorizonJ2() {
        return sumHorizonJ2;
    }

    private void aiTrainingGames() throws Exception {
        Kube k = new Kube(true);
        while (getnGamesFinished() < getNbGames()) {
            // Phase 1
            ArrayList<Integer> horizonReachedJ1 = new ArrayList<>();
            ArrayList<Integer> horizonReachedJ2 = new ArrayList<>();
            k.init(new moveSetHeuristique(50), new moveSetHeuristique(50));
            k.getP1().getAI().constructionPhase();
            k.updatePhase();
            k.getP2().getAI().constructionPhase();
            k.updatePhase();
            // Phsae 2
            k.setCurrentPlayer(k.getRandomPlayer());
            while (k.canCurrentPlayerPlay()) {  
                Move move = k.getCurrentPlayer().getAI().nextMove();
                if (k.getCurrentPlayer() == k.getP1()) {
                    horizonReachedJ1.add(k.getCurrentPlayer().getAI().getHorizonMax());
                } else {
                    horizonReachedJ2.add(k.getCurrentPlayer().getAI().getHorizonMax());
                }
                k.playMove(move);
            }
            if (getnGamesFinished() >= getNbGames()) {
                return;
            }
            if (k.getCurrentPlayer() == k.getP1()) {
                upWinJ2();
            } else {
                upWinJ1();
            }
            addNbMovesJ1(k.getP1().getAI().getNbMoves());
            addNbMovesJ2(k.getP2().getAI().getNbMoves());
            incrnGamesFinished();
            for (int i : horizonReachedJ1){
                if (i < 20){
                    addToSumHorizonJ1(i);
                }
            }
            for (int i : horizonReachedJ2){
                if (i < 20){
                    addToSumHorizonJ2(i);
                }
            }
            System.out.print("\rPartie n°" + getnGamesFinished() + " finie");
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
            if (k.getP1().getAvailableToBuild().equals(k.getP2().getAvailableToBuild())) {
                break;
            }
        }
        // System.out.println("Seed avec une même distribution de cube pour les joueurs
        // :" + seed);
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
    private void randomFillMountain(Mountain m, ArrayList<ModelColor> bag) {
        ModelColor c;
        int n_white = 2, n_natural = 2;
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                c = ModelColor.getRandomColor();
                if (c == ModelColor.NATURAL && n_natural > 0) {
                    n_natural--;
                    m.setCase(i, j, c);
                } else if (c == ModelColor.WHITE && n_white > 0) {
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
            m.setCase(p.x, p.y, ModelColor.EMPTY);
            removables = m.removable();
        }
        return removablesSum / 21;

    }

    private float randomAdd(Mountain m) {
        Random r = new Random();
        int nMove = 0;
        float availableSum = 0;
        ModelColor c = ModelColor.NATURAL;
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

    private void testMountain(){
        Kube k = new Kube(true);
        k.setHistory(new History());
        k.setBag(new ArrayList<>());
        while (getnGamesFinished() < getNbGames()) {
        Mountain m = new Mountain(9);
        //Init the base
        m.setCase(8,0, ModelColor.RED);
        m.setCase(8,1, ModelColor.BLUE);
        m.setCase(8,2, ModelColor.YELLOW);
        m.setCase(8,3, ModelColor.GREEN);
        m.setCase(8,4, ModelColor.RED);
        m.setCase(8,5, ModelColor.BLUE);
        m.setCase(8,6, ModelColor.GREEN);
        m.setCase(8,7, ModelColor.GREEN);
        m.setCase(8,8, ModelColor.BLUE);
        k.setK3(m);
        //Init the first IA
        k.setP1(new AI(1,new moveSetHeuristique(30),k));
        
        
        ModelColor[][]mountain= new ModelColor[][]{ {ModelColor.RED,ModelColor.EMPTY,ModelColor.EMPTY,ModelColor.EMPTY,ModelColor.EMPTY,ModelColor.EMPTY},
                                                    {ModelColor.GREEN,ModelColor.RED,ModelColor.EMPTY,ModelColor.EMPTY,ModelColor.EMPTY,ModelColor.EMPTY},
                                                    {ModelColor.BLUE,ModelColor.RED,ModelColor.BLUE,ModelColor.EMPTY,ModelColor.EMPTY,ModelColor.EMPTY},
                                                    {ModelColor.NATURAL,ModelColor.RED,ModelColor.GREEN,ModelColor.WHITE,ModelColor.EMPTY,ModelColor.EMPTY},
                                                    {ModelColor.YELLOW,ModelColor.WHITE,ModelColor.GREEN,ModelColor.YELLOW,ModelColor.GREEN,ModelColor.EMPTY},
                                                    {ModelColor.YELLOW,ModelColor.BLACK,ModelColor.NATURAL,ModelColor.BLACK,ModelColor.YELLOW,ModelColor.BLACK}
                                                };
        Mountain iaMountain = new Mountain(mountain,6);

        HashMap<ModelColor, Integer> bag1 = new HashMap<>();
        bag1.put(ModelColor.RED, 0);
        bag1.put(ModelColor.BLUE, 0);
        bag1.put(ModelColor.YELLOW, 0);
        bag1.put(ModelColor.GREEN, 0);
        bag1.put(ModelColor.BLACK, 0);
        bag1.put(ModelColor.WHITE, 0);
        bag1.put(ModelColor.NATURAL, 0);
        k.getP1().setAvailableToBuild(bag1);
        k.getP1().setMountain(iaMountain);
        k.getP1().validateBuilding();
        k.updatePhase();
        //Init the second IA

        k.setP2(new AI(2, new moveSetHeuristique(30), k));
        HashMap<ModelColor, Integer> bag = new HashMap<>();
        bag.put(ModelColor.RED, 3);
        bag.put(ModelColor.BLUE, 4);
        bag.put(ModelColor.YELLOW, 4);
        bag.put(ModelColor.GREEN, 2);
        bag.put(ModelColor.BLACK, 4);
        bag.put(ModelColor.WHITE, 2);
        bag.put(ModelColor.NATURAL, 2);
        k.getP2().setAvailableToBuild(bag);
        k.getP2().getAI().constructionPhase();

        k.setPhase(2);

        //Phase 2

        k.setCurrentPlayer(k.getRandomPlayer());
        while (k.canCurrentPlayerPlay()) {
            Move move = k.getCurrentPlayer().getAI().nextMove();
            k.playMove(move);
        }
        if (getnGamesFinished() >= getNbGames()) {
            return;
        }
        if (k.getCurrentPlayer() == k.getP1()) {
            upWinJ2();
        } else {
            upWinJ1();
        }
        addNbMovesJ1(k.getP1().getAI().getNbMoves());
        addNbMovesJ2(k.getP2().getAI().getNbMoves());
        incrnGamesFinished();
        System.out.print("\rPartie n°" + getnGamesFinished() + " finie");

    }
}

}
