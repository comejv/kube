package kube;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;

public class Simulation {
    public static void main(String[] args){
        Simulation s = new Simulation();
        s.simulateNumberOfCubeAccessible();

    }

    private void simulateNumberOfCubeAccessible() {
        float nTest = 10000;
        Mountain m = new Mountain(6);
        Kube k = new Kube();
        float removableSum = 0;
        for (int i = 0; i < nTest; i++) {
            k.fillBag();
            randomFillMountain(m, k.getBag());
            removableSum += randomWithdraw(m);
        }
        System.out.println("Au total, sur " + nTest + " simulations, il y a eu en moyenne " + removableSum / nTest + " pieces retirable à chaque coup");
    }

    // A method to fill a moutain with random colors + 2 white and 2 natural
    private void randomFillMountain(Mountain m, ArrayList<Color> bag) {
        Color c;
        int n_white = 2, n_natural = 2;
        for (int i = 0; i < m.getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                c = Color.getRandomColor();
                if (c == Color.NATURAL && n_natural > 0) {
                    n_natural--;
                    m.setCase(i, j, c);
                } else if (c == Color.WHITE && n_white > 0) {
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
            m.setCase(p.x, p.y, Color.EMPTY);
            removables = m.removable();
        }
        return removablesSum / 21;

    }
}
