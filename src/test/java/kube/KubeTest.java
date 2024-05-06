package kube;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import kube.configuration.Config;

import kube.model.Color;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.move.*;

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

        Kube kube = new Kube();
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
    }

    @Test
    public void isPlayableWorkingTest() {

        Kube k = new Kube();
        k.setCurrentPlayer(k.getP1());

        setKubeBase(k);
        setPlayerOneMountain(k);
        setPlayerTwoMountain(k);

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

        // MoveAW
        assertTrue(k.isPlayable(new MoveAW()));

        // MoveMA
        assertTrue(k.isPlayable(new MoveMA(1, 0, Color.RED)));
        assertTrue(k.isPlayable(new MoveMA(1, 1, Color.GREEN)));

        // MoveAA
        assertTrue(k.isPlayable(new MoveAA(Color.GREEN)));
        assertTrue(k.isPlayable(new MoveAA(Color.YELLOW)));
        assertTrue(k.isPlayable(new MoveAA(Color.BLACK)));
        assertTrue(k.isPlayable(new MoveAA(Color.RED)));
        assertTrue(k.isPlayable(new MoveAA(Color.NATURAL)));
        assertTrue(k.isPlayable(new MoveAA(Color.WHITE)));
    }

    @Test
    public void isPlayableNotWorkingTest() {

        Kube k = new Kube();
        k.setCurrentPlayer(k.getP1());

        setKubeBase(k);
        setPlayerOneMountain(k);
        setPlayerTwoMountain(k);

        // MoveMM not removable
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
        assertFalse(k.isPlayable(new MoveAM(7, 4, Color.BLUE)));
        assertFalse(k.isPlayable(new MoveAM(7, 7, Color.RED)));

        // MoveAW not removable
        k.getP1().getAdditional().remove(Color.WHITE);
        assertFalse(k.isPlayable(new MoveAW()));

        // MoveMA not removable
        assertFalse(k.isPlayable(new MoveMA(2, 0, Color.YELLOW)));
        assertFalse(k.isPlayable(new MoveMA(2, 1, Color.WHITE)));
        assertFalse(k.isPlayable(new MoveMA(2, 2, Color.BLUE)));

        // MoveAA not removable
        assertFalse(k.isPlayable(new MoveAA(Color.BLUE)));
    }

    @Test
    public void distributeCubesToPlayersTest() {

        Kube kube = new Kube();
        kube.fillBag();
        kube.distributeCubesToPlayers();
        int sum = 0;
        for (int n : kube.getP1().getAvalaibleToBuild().values()) {
            sum += n;
        }
        assertEquals(21, sum);
        sum = 0;
        for (int n : kube.getP2().getAvalaibleToBuild().values()) {
            sum += n;
        }
        assertEquals(21, sum);
        assertEquals(11, kube.getBag().size());
    }

    @Test
    public void playMoveWorkingTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        kube.setPhase(2);

        // MoveMW
        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMW(1, 1)));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 1));

        // MoveMM
        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMM(1, 0, 7, 0, Color.BLUE)));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 0));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMM(1, 0, 7, 5, Color.BLUE)));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 5));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMM(1, 0, 7, 6, Color.BLUE)));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 6));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMM(1, 0, 7, 7, Color.BLUE)));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 7));

        // MoveAM
        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 0, Color.BLUE)));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 0));
        assertEquals(0, Collections.frequency(kube.getP1().getAdditional(), Color.BLUE));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 5, Color.BLUE)));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 5));
        assertFalse(kube.getP1().getAdditional().contains(Color.BLUE));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 6, Color.BLUE)));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 6));
        assertFalse(kube.getP1().getAdditional().contains(Color.BLUE));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 7, Color.BLUE)));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 7));
        assertFalse(kube.getP1().getAdditional().contains(Color.BLUE));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 0, Color.RED)));
        assertEquals(Color.RED, kube.getK3().getCase(7, 0));
        assertFalse(kube.getP1().getAdditional().contains(Color.RED));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 1, Color.RED)));
        assertEquals(Color.RED, kube.getK3().getCase(7, 1));
        assertFalse(kube.getP1().getAdditional().contains(Color.RED));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 5, Color.RED)));
        assertEquals(Color.RED, kube.getK3().getCase(7, 5));
        assertFalse(kube.getP1().getAdditional().contains(Color.RED));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAM(7, 6, Color.RED)));
        assertEquals(Color.RED, kube.getK3().getCase(7, 6));
        assertFalse(kube.getP1().getAdditional().contains(Color.RED));

        for (int i = 0; i < 7; i++) {
            initPlayMove(kube);
            assertTrue(kube.playMove(new MoveAM(7, i, Color.NATURAL)));
            assertEquals(Color.NATURAL, kube.getK3().getCase(7, i));
            assertFalse(kube.getP1().getAdditional().contains(Color.NATURAL));
        }

        // MoveAW
        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAW()));
        assertFalse(kube.getP1().getAdditional().contains(Color.WHITE));

        // MoveMA
        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMA(1, 0, Color.RED)));
        assertEquals(Color.EMPTY, kube.getP2().getMountain().getCase(1, 0));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditional(), Color.RED));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveMA(1, 1, Color.GREEN)));
        assertEquals(Color.EMPTY, kube.getP2().getMountain().getCase(1, 1));
        assertEquals(1, Collections.frequency(kube.getP2().getAdditional(), Color.GREEN));

        // MoveAA
        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAA(Color.GREEN)));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditional(), Color.GREEN));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditional(), Color.GREEN));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAA(Color.YELLOW)));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditional(), Color.YELLOW));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditional(), Color.YELLOW));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAA(Color.BLACK)));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditional(), Color.BLACK));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditional(), Color.BLACK));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAA(Color.RED)));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditional(), Color.RED));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditional(), Color.RED));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAA(Color.NATURAL)));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditional(), Color.NATURAL));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditional(), Color.NATURAL));

        initPlayMove(kube);
        assertTrue(kube.playMove(new MoveAA(Color.WHITE)));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditional(), Color.WHITE));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditional(), Color.WHITE));
    }
    
    @Test
    public void playMoveNotWorkingTest() {

        Kube kube = new Kube();
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

        // Move a cube on the wrong mountain's cubes
        res = kube.playMove(new MoveMM(0, 0, 7, 2, kube.getP1().getMountain().getCase(0, 0)));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(Color.BLUE, kube.getP1().getMountain().getCase(0, 0));

        // Move a cube with the wrong color
        res = kube.playMove(new MoveMM(0, 0, 7, 0, Color.RED));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(Color.BLUE, kube.getP1().getMountain().getCase(0, 0));
    }

    @Test
    public void testSeededBag() {

        Kube kube = new Kube();
        kube.fillBag(1);
        Kube kube2 = new Kube();
        kube2.fillBag(1);
        for (int i = 0; i < kube.getBag().size(); i++) {
            assertEquals(kube.getBag().get(i), kube2.getBag().get(i));
        }
    }

    @Test
    public void moveSetTest() {

        Kube kube = new Kube();
        kube.fillBag(1);
        kube.fillBase();
        kube.distributeCubesToPlayers();
        Color[][] mountainP1 = new Color[][] {
            { Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY },
            { Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY },
            { Color.EMPTY, Color.BLACK, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY },
            { Color.RED, Color.BLUE, Color.GREEN, Color.EMPTY, Color.EMPTY, Color.EMPTY },
            { Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.EMPTY, Color.EMPTY },
            { Color.RED, Color.BLUE, Color.GREEN, Color.NATURAL, Color.YELLOW, Color.WHITE }
        };

        kube.getP1().getMountain().setMountain(mountainP1);
        kube.getP1().addAdditional(Color.NATURAL);
        kube.getP1().addAdditional(Color.WHITE);
        kube.getP1().addAdditional(Color.YELLOW);
        kube.setCurrentPlayer(kube.getP1());
        ArrayList<Move> moves = kube.moveSet();

        assertTrue(moves.contains(new MoveMM(2, 1, 7, 0, Color.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 2, Color.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 3, Color.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 4, Color.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 5, Color.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 5, Color.BLACK)));
        assertTrue(moves.contains(new MoveMW(5, 5)));
        assertTrue(moves.contains(new MoveAM(7, 0, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 1, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 2, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 3, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 4, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 5, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 6, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 7, Color.NATURAL)));
        assertTrue(moves.contains(new MoveAW()));
        assertTrue(moves.contains(new MoveAM(7, 6, Color.YELLOW)));
        assertTrue(moves.contains(new MoveAM(7, 7, Color.YELLOW)));
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

    public void setPlayerOneMountain(Kube k) {

        Mountain m = new Mountain(3);
        k.getP1().getAdditional().clear();

        m.setCase(1, 0, Color.BLUE);
        m.setCase(1, 1, Color.WHITE);
        m.setCase(2, 0, Color.YELLOW);
        m.setCase(2, 1, Color.WHITE);
        m.setCase(2, 2, Color.BLUE);

        k.getP1().setMountain(m);

        k.getP1().getAdditional().add(Color.BLUE);
        k.getP1().getAdditional().add(Color.RED);
        k.getP1().getAdditional().add(Color.NATURAL);
        k.getP1().getAdditional().add(Color.WHITE);
    }

    public void setPlayerTwoMountain(Kube k) {

        Mountain m = new Mountain(3);
        k.getP2().getAdditional().clear();

        m.setCase(1, 0, Color.RED);
        m.setCase(1, 1, Color.GREEN);
        m.setCase(2, 0, Color.BLACK);
        m.setCase(2, 1, Color.GREEN);
        m.setCase(2, 2, Color.RED);

        k.getP2().setMountain(m);

        k.getP2().getAdditional().add(Color.GREEN);
        k.getP2().getAdditional().add(Color.YELLOW);
        k.getP2().getAdditional().add(Color.BLACK);
        k.getP2().getAdditional().add(Color.RED);
        k.getP2().getAdditional().add(Color.NATURAL);
        k.getP2().getAdditional().add(Color.WHITE);
    }

    public void initPlayMove(Kube kube) {

        kube.getK3().clear();
        setKubeBase(kube);
        setPlayerOneMountain(kube);
        setPlayerTwoMountain(kube);
        kube.setCurrentPlayer(kube.getP1());
    }
}