package kube;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import kube.configuration.Config;

import kube.model.Color;
import kube.model.Kube;


public class KubeTest {


    @Test
    public void testKube() {
        Kube kube = new Kube();
        assertEquals(6, kube.getP1().getMountain().getBaseSize());
        assertEquals(6, kube.getP2().getMountain().getBaseSize());
        assertEquals(9, kube.getK3().getBaseSize());
        assertEquals(0, kube.getHistoric().getDone().size());
        assertEquals(0, kube.getHistoric().getUndone().size());
        assertEquals(0, kube.getHistoric().getFirstPlayer());
        assertEquals(1, kube.getPhase());
    }
    
    @Test
    public void testBag() {
        Kube kube = new Kube();
        kube.fillBag();
        ArrayList<Color> bag = kube.getBag();
        assertEquals(45, bag.size());
        int[] colors = new int[8];
        for (Color c : bag) {
            switch (c) {
                case RED:
                    colors[3]++;
                    break;
                case GREEN:
                    colors[4]++;
                    break;
                case BLUE:
                    colors[5]++;
                    break;
                case YELLOW:
                    colors[6]++;
                    break;
                case BLACK:
                    colors[7]++;
                    break;
                default:
                    assertFalse(true);
            }
        }
        for (int i = 3; i < 8; i++) {
            assertEquals(colors[i], 9);
        }
    }

    @Test
    public void testBase() {
        Kube kube = new Kube();
        kube.fillBag();
        kube.fillBase();
        assertEquals(36, kube.getBag().size());
        Color[] base = new Color [9];
        for (int i = 0; i < 9; i++) {
            base[i] = kube.getK3().getCase(8, i);
        }
        ArrayList<Color> colors = new ArrayList<>();
        for (Color c : base) {
            assertFalse(c == Color.EMPTY);
            if (!colors.contains(c)) {
                colors.add(c);
            }
        }
        assertTrue(colors.size()>=4);

    }
}
