package kube.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kube.model.ModelColor;
import kube.model.Player;
import kube.model.Kube;
import kube.model.action.move.Move;

public class moveSetHeuristique extends MiniMaxAI {
    ArrayList<ModelColor> colors;
    ArrayList<Float> cumulativesProbabilities;
    HashMap<ModelColor, Float> probabilities;

    /**********
     * CONSTRUCTORS
     **********/

    public moveSetHeuristique(int time, Random r) {
        super(time, r);
    }

    public moveSetHeuristique(int time, int seed) {
        super(time, seed);

    }

    public moveSetHeuristique(int time) {
        super(time);
    }

    public moveSetHeuristique() {
        super();
    }

    /**********
     * METHODS
     **********/
    @Override
    public void constructionPhase() {
        getBaseRepartiton();
        for (int i = 0; i < getPlayer(getK3()).getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                ModelColor c = getColorBasedOnProbabilities();
                getPlayer(getK3()).addToMountainFromAvailableToBuild(i, j, c);
                redistributeProbs(c);
            }
        }
        getPlayer(getK3()).validateBuilding();
    }

    @Override
    public int evaluation(Kube k, Player p) {
        return k.moveSet(p).size() + p.getAdditionals().size() - p.getWhiteUsed();
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap) {
        if (movesMap == null || movesMap.size() == 0) {
            ArrayList<Move> moves = getK3().moveSet();
            return moves.get(getR().nextInt(moves.size()));
        }
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    private HashMap<ModelColor, Float> getBaseRepartiton() {
        int baseSize = getK3().getBaseSize();
        float nEmplacements = 0f;
        probabilities = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            probabilities.put(c, 0f);
        }
        for (int i = 1; i < baseSize; i++) {
            ModelColor c1 = getK3().getK3().getCase(baseSize - 1, i);
            ModelColor c2 = getK3().getK3().getCase(baseSize - 1, i - 1);
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
            redistributeProbs(c);
        }
        return probabilities;
    }

    private void redistributeProbs(ModelColor c) {
        if (getPlayer(getK3()).getAvailableToBuild().get(c) == 0) {
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
        float f = getR().nextFloat();
        for (Map.Entry<ModelColor, Float> entry : entryList) {
            f -= entry.getValue();
            if (f < 0) {
                return entry.getKey();
            }
        }
        return entryList.get(entryList.size() - 1).getKey();
    }
}
