package kube;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.MoveMM;
import kube.model.MoveMW;
import kube.model.MoveAM;

public class KubeTest {

    @Test
    public void kubeTest() {
        Kube kube = new Kube();
        assertEquals(6, kube.getP1().getMountain().getBaseSize());
        assertEquals(6, kube.getP2().getMountain().getBaseSize());
        assertEquals(9, kube.getK3().getBaseSize());
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(0, kube.getHistory().getFirstPlayer());
        assertEquals(1, kube.getPhase());
    }

    @Test
    public void fillBagTest() {
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
    public void fillBaseTest() {
        Kube kube;
        int n = 0;
        while (n < 100000) { // Assume that test 100000 times is enough to test the randomness

            kube = new Kube();
            kube.fillBag();
            kube.fillBase();
            assertEquals(36, kube.getBag().size());
            Color[] base = new Color[9];
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
            assertTrue(colors.size() >= 4);
            n++;
        }
    }

    @Test
    public void isPlayableTest() {

        Kube k = new Kube();
        k.setCurrentPlayer(k.getP1());

        Mountain m = new Mountain(3);

        m.setCase(1, 0, Color.BLUE);
        m.setCase(1, 1, Color.WHITE);
        m.setCase(2, 0, Color.YELLOW);
        m.setCase(2, 1, Color.WHITE);
        m.setCase(2, 2, Color.BLUE);

        k.getP1().setMountain(m);

        k.getP1().getAdditional().add(Color.BLUE);
        k.getP1().getAdditional().add(Color.RED);
        k.getP1().getAdditional().add(Color.NATURAL);

        setKubeBase(k);

        // Checking all the possible moves
        // MoveMM
        assertTrue(k.isPlayable(new MoveMM(1, 0, 7, 0, Color.BLUE)));
        assertTrue(k.isPlayable(new MoveMM(1, 0, 7, 6, Color.BLUE)));
        assertTrue(k.isPlayable(new MoveMM(1, 0, 7, 7, Color.BLUE)));

        // MoveMW
        assertTrue(k.isPlayable(new MoveMW(1, 1)));

        // MoveAM
        assertTrue(k.isPlayable(new MoveAM(7, 0, Color.BLUE)));
        assertTrue(k.isPlayable(new MoveAM(7, 6, Color.BLUE)));
        assertTrue(k.isPlayable(new MoveAM(7, 7, Color.BLUE)));
        assertTrue(k.isPlayable(new MoveAM(7, 0, Color.RED)));
        assertTrue(k.isPlayable(new MoveAM(7, 1, Color.RED)));
        assertTrue(k.isPlayable(new MoveAM(7, 0, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 1, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 2, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 3, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 4, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 5, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 6, Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAM(7, 7, Color.NATURAL)));

        // Checking all the impossible moves
        // Placing on existing cubes
        assertFalse(k.isPlayable(new MoveMM(1, 0, 8, 0, Color.BLUE)));
        assertFalse(k.isPlayable(new MoveAM(8, 6, Color.BLUE)));

        // Move not removable
        assertFalse(k.isPlayable(new MoveMM(0, 0, 7, 0, Color.BLUE)));
        assertFalse(k.isPlayable(new MoveMM(2, 2, 7, 0, Color.BLUE)));

        // MoveMM not compatible
        assertFalse(k.isPlayable(new MoveMM(1, 0, 7, 1, Color.BLUE)));
        assertFalse(k.isPlayable(new MoveMM(1, 0, 7, 2, Color.BLUE)));

        // MoveMW not removable
        assertFalse(k.isPlayable(new MoveMW(0, 0)));
        assertFalse(k.isPlayable(new MoveMW(2, 1)));

        // MoveAM not removable
        assertFalse(k.isPlayable(new MoveAM(7, 1, Color.GREEN)));
        assertFalse(k.isPlayable(new MoveAM(7, 2, Color.YELLOW)));

        // MoveAM not compatible
        assertFalse(k.isPlayable(new MoveAM(7, 5, Color.BLUE)));
        assertFalse(k.isPlayable(new MoveAM(7, 7, Color.RED)));
    }

    @Test
    public void distributeCubesToPlayersTest() {
        Kube kube = new Kube();
        kube.fillBag();
        kube.distributeCubesToPlayers();
        assertEquals(21, kube.getP1().getAvalaibleToBuild().size());
        assertEquals(21, kube.getP2().getAvalaibleToBuild().size());
        assertEquals(11, kube.getBag().size());
    }

    @Test
    public void playMoveTest() {
        Kube kube = new Kube();
        kube.fillBag();
        setKubeBase(kube);
        kube.setCurrentPlayer(kube.getP1());
        kube.setPhase(2);
        kube.getP1().getMountain().setCase(0, 0, Color.BLUE);

        // Move to a place there is already a cube
        boolean res = kube.playMove(new MoveMM(0, 0, 8, 0, kube.getP1().getMountain().getCase(0, 0)));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(Color.BLUE, kube.getP1().getMountain().getCase(0, 0));

        // Move a cube where it should not be
        res = kube.playMove(new MoveMM(0, 0, 7, 8, kube.getP1().getMountain().getCase(0, 0)));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(Color.BLUE, kube.getP1().getMountain().getCase(0, 0));

        // Move a cube on top of the wrong cube
        res = kube.playMove(new MoveMM(0, 0, 7, 2, kube.getP1().getMountain().getCase(0, 0)));

    }

    public void setKubeBase(Kube k) {

        k.getK3().setCase(8, 0, Color.BLUE);
        k.getK3().setCase(8, 1, Color.RED);
        k.getK3().setCase(8, 2, Color.GREEN);
        k.getK3().setCase(8, 3, Color.YELLOW);
        k.getK3().setCase(8, 4, Color.BLACK);
        k.getK3().setCase(8, 5, Color.BLACK);
        k.getK3().setCase(8, 6, Color.NATURAL);
        k.getK3().setCase(8, 7, Color.BLUE);
        k.getK3().setCase(8, 8, Color.GREEN);
    }
}
