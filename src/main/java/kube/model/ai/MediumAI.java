package kube.model.ai;

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

public class MediumAI extends MiniMaxAI {
    ArrayList<ModelColor> colors;
    ArrayList<Float> cumulativeProbabilities;
    HashMap<ModelColor, Float> probabilities;
    HashMap<ModelColor, Integer> enemyPieces;

    /**********
     * CONSTRUCTORS
     **********/

    public MediumAI(int time, Random r) {
        super(time, r);
    }

    public MediumAI(int time, int seed) {
        super(time, seed);

    }

    public MediumAI(int time) {
        super(time);
    }

    public MediumAI() {
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
        List<Map.Entry<Move, Integer>> entries = new ArrayList<>(movesMap.entrySet());
        Collections.sort(entries, (e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()));
        for (Map.Entry<Move, Integer> entry : entries) {
            if (entry.getValue() > 0){
                return entry.getKey();
            }
        }
        return entries.get(entries.size() - 1).getKey();
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
        for (ModelColor c : probabilities.keySet()) {
            float diff = ((float) (getPlayer(k3).getAvailableToBuild().get(c) + 1) / (float) (enemyPieces.get(c) + 1));
            probabilities.put(c, probabilities.get(c) * probabilities.get(c) * diff);
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
        float sum = 0;
        for (Map.Entry<ModelColor, Float> entry : entryList) {
            sum += entry.getValue();
        }
        entryList.sort(Map.Entry.comparingByValue());
        float f = getRandom().nextFloat() * sum;
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
            int j = 0;
            int k = i;
            while (j <= k) {
                if (m.getCase(i, j) == ModelColor.EMPTY) {
                    return new Point(i, j);
                }
                j++;
                if (m.getCase(i, k) == ModelColor.EMPTY) {
                    return new Point(i, k);
                }
                k--;
            }
        }
        return null;
    }
}
