package kube.model.ai.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.Player;
import kube.model.action.move.Move;
import kube.model.ai.MiniMaxAI;

public class midLevelAI extends MiniMaxAI {

    ArrayList<ModelColor> colors;
    ArrayList<Float> cumulativesProbabilities;
    HashMap<ModelColor, Float> probabilities;

    /**********
     * CONSTRUCTORS
     **********/

    public midLevelAI(int time, Random r) {
        super(time, r);
    }

    public midLevelAI(int time, int seed) {
        super(time, seed);

    }

    public midLevelAI(int time) {
        super(time);
    }

    public midLevelAI() {
        super();
    }

    /**********
     * METHODS
     **********/
    @Override
    public void constructionPhase(Kube k3) {
        getBaseRepartiton(k3);
        for (int i = 0; i < getPlayer(k3).getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                ModelColor c = getColorBasedOnProbabilities();
                getPlayer(k3).addToMountainFromAvailableToBuild(i, j, c);
                redistributeProbs(c, k3);
            }
        }
    }

    @Override
    public int evaluation(Kube k, Player p) {
        return p.getPlayableColors().size() + p.getAdditionals().size();
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
        probabilities = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            probabilities.put(c, 0f);
        }
        for (int i = 0; i < baseSize; i++) {
            ModelColor c = k3.getMountain().getCase(baseSize - 1, i);
            probabilities.put(c, probabilities.get(c) + (float) 1 / baseSize);
        }
        for (ModelColor c : ModelColor.getAllColored()) {
            redistributeProbs(c, k3);
        }
        return probabilities;
    }

    private void redistributeProbs(ModelColor c, Kube k3) {
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
}
