package kube.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kube.model.ModelColor;
import kube.model.Mountain;
import kube.model.Player;
import kube.configuration.Config;
import kube.model.AI;
import kube.model.Kube;
import kube.model.action.move.Move;

public class betterConstructV3 extends MiniMaxAI {

    ArrayList<Mountain> mountainsA;
    ArrayList<Mountain> mountainsB;
    ArrayList<ModelColor> colors;
    ArrayList<Float> cumulativesProbabilities;
    HashMap<ModelColor, Float> probabilities;

    int max = 10;
    float maxScore = -10;

    float[] scores = new float[max];

    /**********
     * CONSTRUCTORS
     **********/

    public betterConstructV3(int time, Random r) {
        super(time, r);
    }

    public betterConstructV3(int time, int seed) {
        super(time, seed);

    }

    public betterConstructV3(int time) {
        super(time);
    }

    public betterConstructV3() {
        super();
    }

    /**********
     * METHODS
     **********/
    @Override
    public void constructionPhase(Kube k3) {
        int maxIter = max;
        float bestOfAll = 0;
        Mountain bestOfAllM = null;

        while (maxScore < 0.8 && maxIter-- > 0) {
            mountainsA = new ArrayList<>();
            mountainsB = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                Config.debug("Mountain " + i);
                mountainsA.add(generateMountain(getPlayer(k3), k3));
                mountainsB.add(generateMountain(getOtherPlayer(k3), k3));

            }
            Thread t1 = new Thread(
                    new MountainTester(k3, mountainsA.subList(0, max / 2), mountainsB));
            Thread t2 = new Thread(
                    new MountainTester(k3, mountainsA.subList(max / 2, max), mountainsB));

            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] >= maxScore) {
                    maxScore = scores[i];
                    getPlayer(k3).setMountain(mountainsA.get(i).clone());
                    if (maxScore > bestOfAll) {
                        bestOfAll = maxScore;
                        bestOfAllM = mountainsA.get(i).clone();
                    }
                }
            }
        }
        if (maxIter == 0) {
            getPlayer(k3).setMountain(bestOfAllM);
        }
    }

    public Mountain generateMountain(Player p, Kube k3) {
        AI bot = transformPlayer(p.clone(), k3);
        bot.setAvailableToBuild(new HashMap<>(p.getAvailableToBuild()));
        if (p.getIsMountainValidated()) {
            bot.setIsMountainValidated(false);
            for (int i = 0; i < bot.getMountain().getBaseSize(); i++) {
                for (int j = 0; j <= i; j++) {
                    ModelColor c = bot.removeFromMountainToAvailableToBuild(i, j);
                    bot.getAvailableToBuild().put(c, bot.getAvailableToBuild().get(c) + 1);
                }
            }
        }
        Kube kclone = k3.clone();
        switch (bot.getId()){
        case 1:
            kclone.setP1(bot);
        case 2:
            kclone.setP2(bot);
            break;
        }

        return bot.getAI().constructMountain(kclone);
    }

    @Override
    public Mountain constructMountain(Kube k3) {
        Integer[] startPoint;
        getBaseRepartiton(k3);
        setJokers(k3, getRandom());
        while (!getPlayer(k3).isMountainFull()) {
            ModelColor c = getColorBasedOnProbabilities();
            startPoint = getStartPoint(k3);
            getPlayer(k3).addToMountainFromAvailableToBuild(startPoint[0], startPoint[1], c);
            redistributeProbs(k3, c);
        }
        return getPlayer(k3).getMountain();
    }

    @Override
    public int evaluation(Kube k, Player p) {
        return k.moveSet(p).size() + p.getAdditionals().size() - p.getWhiteUsed();
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap, Kube k3) {
        if (movesMap == null || movesMap.size() == 0) {
            ArrayList<Move> moves = k3.moveSet();
            return moves.get(getRandom().nextInt(moves.size()));
        }
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    private HashMap<ModelColor, Float> getBaseRepartiton(Kube k3) {
        int baseSize = k3.getBaseSize();
        float nEmplacements = 0f;
        probabilities = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColored()) {
            probabilities.put(c, 0f);
        }
        for (int i = 1; i < baseSize; i++) {
            ModelColor c1 = k3.getK3().getCase(baseSize - 1, i);
            ModelColor c2 = k3.getK3().getCase(baseSize - 1, i - 1);
            if (c1 != c2) {
                probabilities.put(c1, probabilities.get(c1) + 1);
                probabilities.put(c2, probabilities.get(c2) + 1);
                nEmplacements += 2;
            } else {
                probabilities.put(c1, probabilities.get(c1) + 1);
                nEmplacements += 1;
            }
        }
        for (ModelColor c : ModelColor.getAllColored()) {
            probabilities.put(c, probabilities.get(c) / nEmplacements);
        }
        for (ModelColor c : ModelColor.getAllColored()) {
            redistributeProbs(k3, c);
        }
        return probabilities;
    }

    private void redistributeProbs(Kube k3, ModelColor c) {
        if (getPlayer(k3).getAvailableToBuild().get(c) == 0) {
            float probs = probabilities.get(c);
            probabilities.remove(c);
            for (ModelColor col : probabilities.keySet()) {
                probabilities.put(col, probabilities.get(col) + (probs / probabilities.size()));
            }
        }
    }

    private ModelColor getColorBasedOnProbabilities() {
        List<Map.Entry<ModelColor, Float>> entryList = new ArrayList<>(probabilities.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        float f = getRandom().nextFloat();
        for (Map.Entry<ModelColor, Float> entry : entryList) {
            f -= entry.getValue();
            if (f < 0) {
                return entry.getKey();
            }
        }
        return entryList.get(entryList.size() - 1).getKey();
    }

    private void setJokers(Kube k3, Random r) {
        ArrayList<ModelColor> jokers = new ArrayList<>();
        Player p = getPlayer(k3);
        jokers.add(ModelColor.NATURAL);
        jokers.add(ModelColor.WHITE);
        jokers.add(ModelColor.NATURAL);
        jokers.add(ModelColor.WHITE);

        Collections.shuffle(jokers, r);

        p.addToMountainFromAvailableToBuild(3, r.nextInt(4), jokers.remove(0));
        p.addToMountainFromAvailableToBuild(4, r.nextInt(2), jokers.remove(0));
        p.addToMountainFromAvailableToBuild(4, r.nextInt(2) + 2, jokers.remove(0));
        if (r.nextInt(2) == 0) {
            p.addToMountainFromAvailableToBuild(5, r.nextInt(2), jokers.remove(0));
        } else {
            p.addToMountainFromAvailableToBuild(5, r.nextInt(2), jokers.remove(0));
        }
    }

    private Integer[] getStartPoint(Kube k3) {
        Mountain m = getPlayer(k3).getMountain();
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j <= i; j++) {
                if (m.getCase(i, j) == ModelColor.EMPTY) {
                    return new Integer[] { i, j };
                }
            }
        }
        return null;
    }

    private AI transformPlayer(Player p, Kube k3) {
        AI ai = new AI(p.getId(), new betterConstructV3(500, getRandom()));
        ai.setAdditionals(new ArrayList<ModelColor>(p.getAdditionals()));
        ai.setName(p.getName());
        ai.setUsedPiece(new HashMap<ModelColor, Integer>(p.getUsedPiece()));
        ai.setMountain(p.getMountain().clone());
        ai.setIsMountainValidated(p.getIsMountainValidated());
        if(p.getIsMountainValidated()){
            ai.setMountain(p.getMountain().clone());
        }else{
        ai.setAvailableToBuild(new HashMap<ModelColor, Integer>(p.getAvailableToBuild()));
        }
        return ai;
    }

    private synchronized void setScore(int i, float ratio) {
        this.scores[i] = ratio;
    }

    private class MountainTester implements Runnable {

        List<Mountain> contender;
        ArrayList<Mountain> toFight;
        private int score;
        private int nbGames;
        private Kube originalKube, k;

        private MountainTester(Kube k, List<Mountain> contender, ArrayList<Mountain> toFight) {
            this.originalKube = k;
            this.contender = contender;
            this.toFight = toFight;
        }

        @Override
        public void run() {
            for (Mountain a : contender) {
                for (Mountain m : toFight) {
                    k = originalKube.clone();
                    k.setP1(transformPlayer(getPlayer(k).clone(), k));
                    k.getP1().setMountain(a.clone());
                    k.setP2(transformPlayer(getOtherPlayer(k).clone(), k));
                    k.getP2().setMountain(m.clone());
                    k.setCurrentPlayer(k.getRandomPlayer());
                    k.getP1().validateBuilding();
                    k.getP2().validateBuilding();
                    k.updatePhase();
                    score += playGame(k);
                    nbGames += 1;
                }
                setScore(contender.indexOf(a), getRatio());
            }
        }

        private int playGame(Kube k) {
            int win = 0;
            try {
                while (!k.canCurrentPlayerPlay()) {

                    Move m = k.getCurrentPlayer().getAI().nextMove(k);
                    k.playMove(m);

                }
            } catch (Exception e) {
                return -10;
            }
            if (k.getCurrentPlayer() == k.getP1()) {
                win += 1;
            } else {

            }
            return win;
        }

        public float getRatio() {
            return (float) score / (nbGames);
        }

    }
}
