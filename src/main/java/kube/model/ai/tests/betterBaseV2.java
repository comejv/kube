package kube.model.ai.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.Random;

import kube.model.ModelColor;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.ai.MiniMaxAI;
import kube.model.Kube;

public class betterBaseV2 extends MiniMaxAI {

    HashMap<ModelColor, Float> probabilities;
    private Player p;

    /**********
     * CONSTRUCTORS
     **********/

    public betterBaseV2(int time, Random r) {
        super(time, r);
    }

    public betterBaseV2(int time, int seed) {
        super(time, seed);

    }

    public betterBaseV2(int time) {
        super(time);
    }

    public betterBaseV2() {
        super();
    }

    /**********
     * METHODS
     **********/

    @Override
    public int evaluation(Kube k, Player p) {
        return k.moveSet(p).size() + p.getAdditionals().size() - p.getWhiteUsed();
    }

    @Override
    public void constructionPhase(Kube k3) {
        p = getPlayer(k3);
        getBaseRepartition(k3);
        ModelColor c;
        Integer[] startPoint;
        int[] posed = { 0, 0, 0, 0, 0, 0 };
        int a = 0;
        int i, j, old_j;
        setJokers(getRandom());
        for (int k = 0; k < 3; k++) {
            startPoint = getStartPoint();
            i = startPoint[0];
            old_j = j = startPoint[1];
            c = getColorBasedOnProbabilities();
            poseColor: while (p.getAvailableToBuild().get(c) > 0 && i < p.getMountain().getBaseSize()) {
                if (old_j != i) {
                    do {
                        j = old_j + getRandom().nextInt(2);
                        try {
                            if (p.getMountain().getCase(i, j) != ModelColor.EMPTY
                                    && p.getMountain().getCase(i, j + 1) != ModelColor.EMPTY) {
                                break poseColor;
                            }
                        } catch (Exception e) {
                            i--;
                            break poseColor;
                        }
                    } while (p.getMountain().getCase(i, j) != ModelColor.EMPTY);
                }
                if (p.getMountain().getCase(i, j) != ModelColor.EMPTY) {
                    i--;
                    break;
                }
                p.addToMountainFromAvailableToBuild(i, j, c);
                posed[i]++;
                old_j = j;
                i++;
            }
            redistributeProbs(k3, c);
            if (a < 6 && posed[a] == a + 1) {
                a++;
            }
        }
        while (!p.isMountainFull()) {
            c = getColorBasedOnProbabilities();
            startPoint = getStartPoint();
            getPlayer(k3).addToMountainFromAvailableToBuild(startPoint[0], startPoint[1], c);
            redistributeProbs(k3, c);
        }
    }

    /**
     * Get probabilities of the colors on the base
     * 
     * @param k3 Kube object
     * @return HashMap<ModelColor, Float> of probabilities
     */

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

    /**
     * Redistribute the probabilities of the colors
     * 
     * @param k3 Kube object
     * @param c  ModelColor
     */
    private void redistributeProbs(Kube k3, ModelColor c) {
        if (getPlayer(k3).getAvailableToBuild().get(c) == 0) {
            float probs = probabilities.get(c);
            probabilities.remove(c);
            for (ModelColor col : probabilities.keySet()) {
                probabilities.put(col, probabilities.get(col) + (probs / probabilities.size()));
            }
        }
    }

    /**
     * @return the first empty point on the mountain
     */
    private Integer[] getStartPoint() {
        Mountain m = p.getMountain();
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j <= i; j++) {
                if (m.getCase(i, j) == ModelColor.EMPTY) {
                    return new Integer[] { i, j };
                }
            }
        }
        return null;
    }

    /**
     * Set the jokers at some predisposed positions
     * 
     * @param r Random object for seeding
     */

    private void setJokers(Random r) {
        ArrayList<ModelColor> jokers = new ArrayList<>();
        jokers.add(ModelColor.NATURAL);
        jokers.add(ModelColor.WHITE);
        jokers.add(ModelColor.NATURAL);
        jokers.add(ModelColor.WHITE);

        Collections.shuffle(jokers, r);

        p.addToMountainFromAvailableToBuild(3, r.nextInt(2), jokers.remove(0));
        p.addToMountainFromAvailableToBuild(4, r.nextInt(2), jokers.remove(0));
        p.addToMountainFromAvailableToBuild(4, r.nextInt(2) + 3, jokers.remove(0));
        if (r.nextInt(2) == 0) {
            p.addToMountainFromAvailableToBuild(5, 0, jokers.remove(0));
        } else {
            p.addToMountainFromAvailableToBuild(5, 5, jokers.remove(0));
        }
    }

    /**
     * Get one of the color with the highest probability
     * 
     * @return ModelColor based on probabilities
     */

    private ModelColor getColorBasedOnProbabilities() {
        float r = getRandom().nextFloat();
        float sum = 0;
        for (ModelColor c : probabilities.keySet()) {
            sum += probabilities.get(c);
            if (r < sum) {
                return c;
            }
        }
        return null;
    }
}
