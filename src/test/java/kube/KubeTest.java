package kube;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
        ArrayList<Color> bag = kube.getBag();
        assertEquals(9, bag.size());
        int[] colors = new int[8];
        for (Color c : bag) {
            switch (c) {
                case WHITE:
                    colors[1]++;
                    break;
                case NATURAL:
                    colors[2]++;
                    break;
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
        for (int i = 1; i < 8; i++) {
            assertTrue(colors[i] >= 9);
        }
    }
}
