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

public class betterBase extends MiniMaxAI {

    HashMap<ModelColor, Float> probabilities;
    private Player p;

    /**********
     * CONSTRUCTORS
     **********/

    public betterBase(int time, Random r) {
        super(time, r);
    }

    public betterBase(int time, int seed) {
        super(time, seed);

    }

    public betterBase(int time) {
        super(time);
    }

    public betterBase() {
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
        /*
         * -On recupere la couleur la plus présente sur la base avec getBaseRepartition
         * -On créer une colone avec cette couleur
         * -On refait ça en démarant la colone sur la deuxieme ligne avec la deuxieme
         * couleur la plus présente
         * -Puis la troisieme
         * - On remplie les trous avec les pieces restantes en ne mettant qu'un
         * ModelColor.NATURAL ou ModeColor.WHITE par ligne
         */
        p = getPlayer(k3);
        getBaseRepartiton(k3);

        ModelColor c;
        Integer[] startPoint;
        int[] posed = { 0, 0, 0, 0, 0, 0 };
        int a = 0;
        int i, j, oldj;

        setJokers(getRandom());

        for (int k = 0; k < 2; k++) {

            startPoint = getStartPoint();
            i = startPoint[0];
            oldj = j = startPoint[1];

            c = getColorBasedOnProbabilities();
            poseColor: while (p.getAvailableToBuild().containsKey(c) && p.getAvailableToBuild().get(c) > 0 && i < p.getMountain().getBaseSize()) {

                if (oldj != i) {
                    do {
                        j = getRandom().nextInt(i + 1);
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
                oldj = j;
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

    private HashMap<ModelColor, Float> getBaseRepartiton(Kube k3) {
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
        HashMap<ModelColor, Integer> otherPlayerPieces = getOtherPlayerPieces(k3);
        modulateProbabilities(probabilities, otherPlayerPieces);

        return probabilities;
    }

    private void modulateProbabilities(HashMap<ModelColor, Float> probabilities,
            HashMap<ModelColor, Integer> otherPlayerPieces) {
        int maxPieces = 0;
        for (int count : otherPlayerPieces.values()) {
            if (count > maxPieces) {
                maxPieces = count;
            }
        }
        // Apply modulation
        for (ModelColor c : probabilities.keySet()) {
            int pieces = otherPlayerPieces.getOrDefault(c, 0);
            float factor = (float) (maxPieces - pieces) / maxPieces;
            probabilities.put(c, probabilities.get(c) * factor);
        }

        // Normalize probabilities
        float total = 0f;
        for (float prob : probabilities.values()) {
            total += prob;
        }

        if (total > 0) {
            for (ModelColor c : probabilities.keySet()) {
                probabilities.put(c, probabilities.get(c) / total);
            }
        }
    }

    private HashMap<ModelColor, Integer> getOtherPlayerPieces(Kube k3) {
        HashMap<ModelColor, Integer> otherPlayerPieces = new HashMap<>();
        for (ModelColor c : ModelColor.getAllColored()) {
            otherPlayerPieces.put(c, 0);
        }
        Player otherP = getOtherPlayer(k3);
        if(otherP.getIsMountainValidated()){
            otherPlayerPieces = new HashMap<>(otherP.getAvailableToBuild());
            for (int i = 0; i < otherP.getMountain().getBaseSize(); i++) {
                for (int j = 0; j <= i; j++) {
                    ModelColor color = otherP.getMountain().getCase(i, j);
                    if (color != ModelColor.EMPTY) {
                        otherPlayerPieces.put(color, otherPlayerPieces.getOrDefault(color, 0) + 1);
                    }
                }
            }
        }else{
            otherPlayerPieces = new HashMap<>(otherP.getAvailableToBuild());
        }
        return otherPlayerPieces;
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

    /*
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

    ModelColor getColorBasedOnProbabilities() {
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
