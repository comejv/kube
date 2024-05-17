package kube;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import kube.model.ModelColor;
import kube.model.Kube;
import kube.model.Mountain;

public class Simulation {
    public static void main(String[] args) {
        Simulation s = new Simulation();
        s.simulateNumberOfCubeWithdrawable();
        s.simulateNumberOfSlotAvailable();
    }

    private void simulateNumberOfSlotAvailable() {
        float nTest = 100000;
        float availaibleSum = 0;
        for (int i = 0; i < nTest; i++) {
            Kube k = new Kube();
            Mountain m = k.getK3();
            k.fillBag();
            k.fillBase();
            availaibleSum += randomAdd(m);
        }
        System.out.println("Au total, sur " + (int) nTest + " simulations, il y a eu en moyenne " + availaibleSum / nTest
        + " cases disponible à chaque coup");
    }

    private void simulateNumberOfCubeWithdrawable() {
        float nTest = 100000;
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
}
