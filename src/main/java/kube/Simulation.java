package kube;

// Import kube classes
import kube.model.AI;
import kube.model.History;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.Mountain;
import kube.model.action.move.Move;
import kube.model.ai.EasyAI;
import kube.model.ai.ExpertAI;
import kube.model.ai.HardAI;
import kube.model.ai.MediumAI;
import kube.model.ai.tests.*;

// Import java classes
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Simulation implements Runnable {

    // TODO : refactor the code to remove the unused methods and variables

    /**********
     * ATTRIBUTES
     **********/
    private int winJ1, winJ2, nbMoveJ1, nbMoveJ2, nbGames, nGamesFinished, sumHorizonJ1, sumHorizonJ2;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class Simulation
     * 
     * @param nbGames the number of games to simulate
     */
    public Simulation(int nbGames) {
        this.nbGames = nbGames;
    }

    /**********
     * GETTERS
     **********/

    public int getnGamesFinished() {
        return nGamesFinished;
    }

    public int getNbGames() {
        return nbGames;
    }

    /**********
     * METHODS
     **********/

    /**
     * Main method of the program (simulation of games between two AI)
     * 
     * @param args the arguments
     */
    public static void main(String[] args) throws Exception {

        int nbGames, nbThreads;
        Simulation s;
        Thread[] threads;

        nbGames = 1000;
        nbThreads = 8;

        try {
            nbGames = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Nombre de parties par défaut: " + nbGames);
        }

        s = new Simulation(nbGames);

        s.winJ1 = 0;
        s.winJ2 = 0;
        s.nbMoveJ1 = 0;
        s.nbMoveJ2 = 0;

        threads = new Thread[nbThreads];
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

    @Override
    public void run() {
        try {
            aiTrainingGames();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to increment the number of wins of the first player
     *
     * @return void
     */
    synchronized void incrementWinJ1() {
        winJ1++;
    }

    /**
     * Method to increment the number of wins of the second player
     * 
     * @return void
     */
    synchronized void incrementWinJ2() {
        winJ2++;
    }

    /**
     * Method to add a number of moves to the first player
     * 
     * @param n the number of moves to add
     * @return void
     */
    synchronized void addNbMovesJ1(int n) {
        nbMoveJ1 += n;
    }

    /**
     * Method to add a number of moves to the second player
     * 
     * @param n the number of moves to add
     * @return void
     */
    synchronized void addNbMovesJ2(int n) {
        nbMoveJ2 += n;
    }

    /**
     * Method to add a number to the sum of horizon of the first player
     * 
     * @param n the number to add
     * @return void
     */
    synchronized void addToSumHorizonJ1(int n) {
        sumHorizonJ1 += n;
    }

    /**
     * Method to add a number to the sum of horizon of the second player
     * 
     * @param h the number to add
     * @return void
     */
    synchronized void addToSumHorizonJ2(int h) {
        sumHorizonJ2 += h;
    }

    /**
     * Method to increment the number of games finished
     * 
     * @return void
     */
    synchronized void incrnGamesFinished() {
        nGamesFinished++;
    }

    private void aiTrainingGames() throws Exception {
        Kube k = new Kube(true);
        while (getnGamesFinished() < getNbGames()) {
            // Phase 1
            ArrayList<Integer> horizonReachedJ1 = new ArrayList<>();
            ArrayList<Integer> horizonReachedJ2 = new ArrayList<>();
            k.init(new EasyAI(50), new MediumAI(50));
            k.getP1().getAI().constructionPhase(k);
            k.getP1().validateBuilding();
            k.updatePhase();
            k.getP2().getAI().constructionPhase(k);
            k.getP2().validateBuilding();
            k.updatePhase();
            // Phsae 2
            // System.out.println(k.getP1());
            // System.out.println(k.getP2());
            k.setCurrentPlayer(k.getRandomPlayer());
            while (k.canCurrentPlayerPlay()) {
                Move move = k.getCurrentPlayer().getAI().nextMove(k);
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
                incrementWinJ2();
            } else {
                incrementWinJ1();
            }
            addNbMovesJ1(k.getP1().getAI().getNbMoves());
            addNbMovesJ2(k.getP2().getAI().getNbMoves());
            incrnGamesFinished();
            for (int i : horizonReachedJ1) {
                if (i < 20) {
                    addToSumHorizonJ1(i);
                }
            }
            for (int i : horizonReachedJ2) {
                if (i < 20) {
                    addToSumHorizonJ2(i);
                }
            }
            System.out.print("\rPartie n°" + getnGamesFinished() + " finie");
        }
    }

    /**
     * Method to test the seeded random
     */
    private void testSeededRandom() {
        int seed = 180;
        Kube k = new Kube(true);
        Kube k2 = new Kube(true);
        k.init(new randomAI(0, 1), new randomAI(0, 1), seed);
        k2.init(new randomAI(0, 1), new randomAI(0, 1), seed);

        k.getCurrentPlayer().getAI().constructionPhase(k);
        k.updatePhase();
        k.getCurrentPlayer().getAI().constructionPhase(k);

        k2.getCurrentPlayer().getAI().constructionPhase(k);
        k2.updatePhase();
        k2.getCurrentPlayer().getAI().constructionPhase(k);

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

    private void testMountain() {
        Kube k = new Kube(true);
        k.setHistory(new History());
        k.setBag(new ArrayList<>());
        while (getnGamesFinished() < getNbGames()) {
            Mountain m = new Mountain(9);
            // Init the base
            m.setCase(8, 0, ModelColor.RED);
            m.setCase(8, 1, ModelColor.BLUE);
            m.setCase(8, 2, ModelColor.YELLOW);
            m.setCase(8, 3, ModelColor.GREEN);
            m.setCase(8, 4, ModelColor.RED);
            m.setCase(8, 5, ModelColor.BLUE);
            m.setCase(8, 6, ModelColor.GREEN);
            m.setCase(8, 7, ModelColor.GREEN);
            m.setCase(8, 8, ModelColor.BLUE);
            k.setK3(m);
            // Init the first IA
            k.setP1(new AI(1, new ExpertAI(30)));

            Mountain iaMountain = new Mountain(6);

            iaMountain.setCase(0, 0, ModelColor.RED);
            iaMountain.setCase(1, 0, ModelColor.GREEN);
            iaMountain.setCase(2, 0, ModelColor.BLUE);
            iaMountain.setCase(3, 0, ModelColor.NATURAL);
            iaMountain.setCase(4, 0, ModelColor.YELLOW);
            iaMountain.setCase(5, 0, ModelColor.YELLOW);
            iaMountain.setCase(1, 1, ModelColor.RED);
            iaMountain.setCase(2, 1, ModelColor.RED);
            iaMountain.setCase(3, 1, ModelColor.RED);
            iaMountain.setCase(4, 1, ModelColor.WHITE);
            iaMountain.setCase(5, 1, ModelColor.BLACK);
            iaMountain.setCase(2, 2, ModelColor.BLUE);
            iaMountain.setCase(3, 2, ModelColor.GREEN);
            iaMountain.setCase(4, 2, ModelColor.GREEN);
            iaMountain.setCase(5, 2, ModelColor.NATURAL);
            iaMountain.setCase(3, 3, ModelColor.WHITE);
            iaMountain.setCase(4, 3, ModelColor.YELLOW);
            iaMountain.setCase(5, 3, ModelColor.BLACK);
            iaMountain.setCase(4, 4, ModelColor.GREEN);
            iaMountain.setCase(5, 4, ModelColor.YELLOW);
            iaMountain.setCase(5, 5, ModelColor.BLACK);

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
            // Init the second IA

            k.setP2(new AI(2, new ExpertAI(30)));
            HashMap<ModelColor, Integer> bag = new HashMap<>();
            bag.put(ModelColor.RED, 3);
            bag.put(ModelColor.BLUE, 4);
            bag.put(ModelColor.YELLOW, 4);
            bag.put(ModelColor.GREEN, 2);
            bag.put(ModelColor.BLACK, 4);
            bag.put(ModelColor.WHITE, 2);
            bag.put(ModelColor.NATURAL, 2);
            k.getP2().setAvailableToBuild(bag);
            k.getP2().getAI().constructionPhase(k);

            k.setPhase(2);

            // Phase 2

            k.setCurrentPlayer(k.getRandomPlayer());
            while (k.canCurrentPlayerPlay()) {
                Move move = k.getCurrentPlayer().getAI().nextMove(k);
                k.playMove(move);
            }
            if (getnGamesFinished() >= getNbGames()) {
                return;
            }
            if (k.getCurrentPlayer() == k.getP1()) {
                incrementWinJ2();
            } else {
                incrementWinJ1();
            }
            addNbMovesJ1(k.getP1().getAI().getNbMoves());
            addNbMovesJ2(k.getP2().getAI().getNbMoves());
            incrnGamesFinished();
            System.out.print("\rPartie n°" + getnGamesFinished() + " finie");

        }
    }

}
