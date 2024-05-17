package kube.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import kube.model.Color;
import kube.model.Kube;
import kube.model.action.move.Move;

public class midLevelAI extends MiniMaxAI {
    ArrayList<Color> colors;
    ArrayList<Float> cumulativesProbabilities;

    /**********
     * CONSTRUCTORS
     **********/

    public midLevelAI(int time, Random r) {
        setR(r);
        setTime(time);
    }

    public midLevelAI(int time, int seed) {
        setR(new Random(seed));
        setTime(time);
    }

    public midLevelAI(int time) {
        setR(new Random());
        setTime(time);
    }

    public midLevelAI() {
        setR(new Random());
        setTime(1000);
    }

    /**********
     * METHODS
     **********/
    @Override
    public void constructionPhase() {
        HashMap<Color, Float> probs = getBaseRepartiton();
        calculateCumulativeSums(probs);
        for (int i = 0; i < getPlayer(getK3()).getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                Color c = getColorBasedOnProbabilities();
                getPlayer(getK3()).addToMountainFromAvailableToBuild(i, j, c);
                redistributeProbs(c);
            }
        }
        //System.out.println(getK3().getK3());
        //System.out.println(getPlayer(getK3()));
        getPlayer(getK3()).validateBuilding();
    }

    @Override
    public int evaluation(Kube k) {
        return getPlayer(k).getPlayableColors().size() + getPlayer(k).getAdditionals().size();
    }

    @Override
    public Move selectMove(HashMap<Move, Integer> movesMap) {
        return Collections.max(movesMap.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    private HashMap<Color, Float> getBaseRepartiton() {
        int baseSize = getK3().getBaseSize();
        HashMap<Color, Float> probs = new HashMap<>();
        for (Color c : Color.getAllColoredAndJokers()) {
            probs.put(c, 0f);
        }
        for (int i = 0; i < baseSize; i++) {
            Color c = getK3().getK3().getCase(baseSize - 1, i);
            probs.put(c, probs.get(c) + (float) 1 / baseSize);
        }
        return probs;
    }

    private void calculateCumulativeSums(HashMap<Color, Float> probabilities) {
        ArrayList<Float> values = new ArrayList<>(probabilities.values());
        Collections.sort(values);
        cumulativesProbabilities = new ArrayList<>();
        colors = new ArrayList<>();
        float sum = 0f;
        for (float f : values) {
            sum += f;
            for (Color c : Color.getAllColoredAndJokers()) {
                if (probabilities.get(c) == f) {
                    cumulativesProbabilities.add(sum);
                    colors.add(c);
                    probabilities.put(c, -1f);
                    break;
                }
            }
        }
        for (Color c : Color.getAllColoredAndJokers()) {
            redistributeProbs(c);
        }
    }

    private void redistributeProbs(Color c) {
        if (getPlayer(getK3()).getAvalaibleToBuild().get(c) == 0) {
            int i = 0;
            while (colors.get(i)!= c) {
                i++;
            }
            float probs = cumulativesProbabilities.remove(i);
            float probsToAdd = 0;
            if (i > 0) {
                probsToAdd = probs - cumulativesProbabilities.get(i - 1);
            }
            colors.remove(i);
            // Adjust the cumulative probabilities list
            for (int j = 0; j < i; j++) {
                cumulativesProbabilities.set(j, cumulativesProbabilities.get(j) + probsToAdd);
            }
        }
    }

    private Color getColorBasedOnProbabilities() {
        float f = getR().nextFloat();
        int i = 0;
        while (cumulativesProbabilities.get(i) <= f) {
            i++;
        }
        return colors.get(i);
    }
}
