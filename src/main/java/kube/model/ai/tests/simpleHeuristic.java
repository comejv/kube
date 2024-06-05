package kube.model.ai.tests;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kube.model.ModelColor;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.Kube;
import kube.model.action.move.Move;
import kube.model.ai.MiniMaxAI;

public class simpleHeuristic extends MiniMaxAI {
    ArrayList<ModelColor> colors;
    ArrayList<Float> cumulativeProbabilities;
    HashMap<ModelColor, Float> probabilities;
    HashMap<ModelColor, Integer> enemyPieces;

    /**********
     * CONSTRUCTORS
     **********/

    public simpleHeuristic(int time, Random r) {
        super(time, r);
    }

    public simpleHeuristic(int time, int seed) {
        super(time, seed);

    }

    public simpleHeuristic(int time) {
        super(time);
    }

    public simpleHeuristic() {
        super();
    }

    /**********
     * METHODS
     **********/

    public HashMap<ModelColor, Integer> getEnemyPieces(Kube k3) {
        Player p;
        if (getPlayer(k3) == k3.getP1()) {
            p = k3.getP2();
        } else {
            p = k3.getP1();
        }
        enemyPieces = new HashMap<>(p.getAvailableToBuild());
        for (int i = 0; i < p.getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                ModelColor c = p.getMountain().getCase(i, j);
                if (c != ModelColor.EMPTY) {
                    enemyPieces.put(c, enemyPieces.get(c) + 1);
                }
            }
        }
        return enemyPieces;
    }

    @Override
    public void constructionPhase(Kube k3) {
        getEnemyPieces(k3);
        Point startPoint;
        getBaseRepartition(k3);
        setJokers(k3, getRandom());
        while (!getPlayer(k3).isMountainFull()) {
            ModelColor c = getColorBasedOnProbabilities();
            startPoint = getStartPoint(k3);
            getPlayer(k3).addToMountainFromAvailableToBuild(startPoint, c);
            redistributeProbs(k3, c);
        }
    }

    @Override
    public int evaluation(Kube k, Player p) {
        return k.moveSet(p).size();
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap, Kube k3) {
        if (movesMap == null || movesMap.size() == 0) {
            ArrayList<Move> moves = k3.moveSet();
            return moves.get(getRandom().nextInt(moves.size()));
        }
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    private HashMap<ModelColor, Float> getBaseRepartition(Kube k3) {
        int baseSize = k3.getBaseSize();
        float nEmplacements = 0f;
        probabilities = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColored()) {
            probabilities.put(c, 0f);
        }
        for (int i = 1; i < baseSize; i++) {
            ModelColor c1 = k3.getMountain().getCase(baseSize - 1, i);
            ModelColor c2 = k3.getMountain().getCase(baseSize - 1, i - 1);
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

    private Point getStartPoint(Kube k3) {
        Mountain m = getPlayer(k3).getMountain();
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j <= i; j++) {
                if (m.getCase(i, j) == ModelColor.EMPTY) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }
}
