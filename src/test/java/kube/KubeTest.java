package kube;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;


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
        initPlayMove(k);

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
        k.setPenality(true);
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
        initPlayMove(k);

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
        k.getP1().getAdditionals().remove(Color.WHITE);
        assertFalse(k.isPlayable(new MoveAW()));

        // MoveMA not removable
        k.setPenality(true);
        assertFalse(k.isPlayable(new MoveMA(2, 0, Color.YELLOW)));
        assertFalse(k.isPlayable(new MoveMA(2, 1, Color.WHITE)));
        assertFalse(k.isPlayable(new MoveMA(2, 2, Color.BLUE)));

        // MoveMA not compatible
        k.setPenality(false);
        assertFalse(k.isPlayable(new MoveMA(1, 0, Color.RED)));
        assertFalse(k.isPlayable(new MoveMA(1, 1, Color.GREEN)));

        // MoveAA not removable
        k.setPenality(true);
        assertFalse(k.isPlayable(new MoveAA(Color.BLUE)));

        // MoveAA not compatible
        k.setPenality(false);
        assertFalse(k.isPlayable(new MoveAA(Color.GREEN)));
        assertFalse(k.isPlayable(new MoveAA(Color.YELLOW)));
        assertFalse(k.isPlayable(new MoveAA(Color.BLACK)));
        assertFalse(k.isPlayable(new MoveAA(Color.RED)));
        assertFalse(k.isPlayable(new MoveAA(Color.NATURAL)));
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

        // MoveMW
        initPlayMove(kube);
        MoveMW mw = new MoveMW(1, 1);
        assertTrue(kube.playMove(mw));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 1));
        int lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mw, kube.getHistory().getDone().get(lastElementIndex));

        // MoveMM
        initPlayMove(kube);
        MoveMM mm = new MoveMM(1, 0, 7, 0, Color.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 0));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        mm = new MoveMM(1, 0, 7, 5, Color.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 5));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        mm = new MoveMM(1, 0, 7, 6, Color.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 6));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        mm = new MoveMM(1, 0, 7, 7, Color.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(Color.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 7));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));

        // MoveAM
        initPlayMove(kube);
        MoveAM am = new MoveAM(7, 0, Color.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 0));
        assertEquals(0, Collections.frequency(kube.getP1().getAdditionals(), Color.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 5, Color.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 5));
        assertFalse(kube.getP1().getAdditionals().contains(Color.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 6, Color.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 6));
        assertFalse(kube.getP1().getAdditionals().contains(Color.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 7, Color.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(Color.BLUE, kube.getK3().getCase(7, 7));
        assertFalse(kube.getP1().getAdditionals().contains(Color.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 0, Color.RED);
        assertTrue(kube.playMove(am));
        assertEquals(Color.RED, kube.getK3().getCase(7, 0));
        assertFalse(kube.getP1().getAdditionals().contains(Color.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 1, Color.RED);
        assertTrue(kube.playMove(am));
        assertEquals(Color.RED, kube.getK3().getCase(7, 1));
        assertFalse(kube.getP1().getAdditionals().contains(Color.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 5, Color.RED);
        assertTrue(kube.playMove(am));
        assertEquals(Color.RED, kube.getK3().getCase(7, 5));
        assertFalse(kube.getP1().getAdditionals().contains(Color.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        am = new MoveAM(7, 6, Color.RED);
        assertTrue(kube.playMove(am));
        assertEquals(Color.RED, kube.getK3().getCase(7, 6));
        assertFalse(kube.getP1().getAdditionals().contains(Color.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));

        for (int i = 0; i < 7; i++) {
            initPlayMove(kube);
            am = new MoveAM(7, i, Color.NATURAL);
            assertTrue(kube.playMove(am));
            assertEquals(Color.NATURAL, kube.getK3().getCase(7, i));
            assertFalse(kube.getP1().getAdditionals().contains(Color.NATURAL));
            lastElementIndex = kube.getHistory().getDone().size() - 1;
            assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        }

        // MoveAW
        initPlayMove(kube);
        MoveAW aw = new MoveAW();
        assertTrue(kube.playMove(aw));
        assertFalse(kube.getP1().getAdditionals().contains(Color.WHITE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aw, kube.getHistory().getDone().get(lastElementIndex));

        // MoveMA
        initPlayMove(kube);
        kube.setPenality(true);
        MoveMA ma = new MoveMA(1, 0, Color.RED);
        assertTrue(kube.playMove(ma));
        assertEquals(Color.EMPTY, kube.getP2().getMountain().getCase(1, 0));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), Color.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(ma, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        kube.setPenality(true);
        ma = new MoveMA(1, 1, Color.GREEN);
        assertTrue(kube.playMove(ma));
        assertEquals(Color.EMPTY, kube.getP2().getMountain().getCase(1, 1));
        assertEquals(1, Collections.frequency(kube.getP2().getAdditionals(), Color.GREEN));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(ma, kube.getHistory().getDone().get(lastElementIndex));

        // MoveAA
        initPlayMove(kube);
        kube.setPenality(true);
        MoveAA aa = new MoveAA(Color.GREEN);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), Color.GREEN));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditionals(), Color.GREEN));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        kube.setPenality(true);
        aa = new MoveAA(Color.YELLOW);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), Color.YELLOW));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditionals(), Color.YELLOW));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        kube.setPenality(true);
        aa = new MoveAA(Color.BLACK);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), Color.BLACK));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditionals(), Color.BLACK));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        kube.setPenality(true);
        aa = new MoveAA(Color.RED);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), Color.RED));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), Color.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        kube.setPenality(true);
        aa = new MoveAA(Color.NATURAL);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), Color.NATURAL));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), Color.NATURAL));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));

        initPlayMove(kube);
        kube.setPenality(true);
        aa = new MoveAA(Color.WHITE);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), Color.WHITE));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), Color.WHITE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
    }
    
    @Test
    public void playMoveNotWorkingTest() {

        Kube kube = new Kube();
        initPlayMove(kube);
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
    public void unPlayTest() {

        Kube kube = new Kube();
        
        // MoveMW
        simpleUnPlayTest(kube, new MoveMW(1, 1));

        // MoveMM
        simpleUnPlayTest(kube, new MoveMM(1, 0, 7, 0, Color.BLUE));

        simpleUnPlayTest(kube, new MoveMM(1, 0, 7, 5, Color.BLUE));

        simpleUnPlayTest(kube, new MoveMM(1, 0, 7, 6, Color.BLUE));

        simpleUnPlayTest(kube, new MoveMM(1, 0, 7, 7, Color.BLUE));

        // MoveAM
        simpleUnPlayTest(kube, new MoveAM(7, 0, Color.BLUE));

        simpleUnPlayTest(kube, new MoveAM(7, 5, Color.BLUE));

        simpleUnPlayTest(kube, new MoveAM(7, 6, Color.BLUE));

        simpleUnPlayTest(kube, new MoveAM(7, 7, Color.BLUE));

        simpleUnPlayTest(kube, new MoveAM(7, 0, Color.RED));

        simpleUnPlayTest(kube, new MoveAM(7, 1, Color.RED));

        simpleUnPlayTest(kube, new MoveAM(7, 5, Color.RED));

        simpleUnPlayTest(kube, new MoveAM(7, 6, Color.RED));

        for (int i = 0; i < 7; i++) {
            simpleUnPlayTest(kube, new MoveAM(7, i, Color.NATURAL));
        }

        // MoveAW
        simpleUnPlayTest(kube, new MoveAW());

        // MoveMA
        simpleUnPlayTest(kube, new MoveMA(1, 0, Color.RED));
        assertTrue(kube.getPenality());

        simpleUnPlayTest(kube, new MoveMA(1, 1, Color.GREEN));
        assertTrue(kube.getPenality());

        // MoveAA
        simpleUnPlayTest(kube, new MoveAA(Color.GREEN));
        assertTrue(kube.getPenality());

        simpleUnPlayTest(kube, new MoveAA(Color.YELLOW));
        assertTrue(kube.getPenality());

        simpleUnPlayTest(kube, new MoveAA(Color.BLACK));
        assertTrue(kube.getPenality());

        simpleUnPlayTest(kube, new MoveAA(Color.RED));
        assertTrue(kube.getPenality());

        simpleUnPlayTest(kube, new MoveAA(Color.NATURAL));
        assertTrue(kube.getPenality());

        simpleUnPlayTest(kube, new MoveAA(Color.WHITE));
        assertTrue(kube.getPenality());
    }

    @Test
    public void rePlayTest() {
    // TODO
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
        kube.getP1().addToAdditionals(Color.NATURAL);
        kube.getP1().addToAdditionals(Color.WHITE);
        kube.getP1().addToAdditionals(Color.YELLOW);
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
        k.getP1().getAdditionals().clear();

        m.setCase(1, 0, Color.BLUE);
        m.setCase(1, 1, Color.WHITE);
        m.setCase(2, 0, Color.YELLOW);
        m.setCase(2, 1, Color.WHITE);
        m.setCase(2, 2, Color.BLUE);

        k.getP1().setMountain(m);

        k.getP1().getAdditionals().add(Color.BLUE);
        k.getP1().getAdditionals().add(Color.RED);
        k.getP1().getAdditionals().add(Color.NATURAL);
        k.getP1().getAdditionals().add(Color.WHITE);
    }

    public void setPlayerTwoMountain(Kube k) {

        Mountain m = new Mountain(3);
        k.getP2().getAdditionals().clear();

        m.setCase(1, 0, Color.RED);
        m.setCase(1, 1, Color.GREEN);
        m.setCase(2, 0, Color.BLACK);
        m.setCase(2, 1, Color.GREEN);
        m.setCase(2, 2, Color.RED);

        k.getP2().setMountain(m);

        k.getP2().getAdditionals().add(Color.GREEN);
        k.getP2().getAdditionals().add(Color.YELLOW);
        k.getP2().getAdditionals().add(Color.BLACK);
        k.getP2().getAdditionals().add(Color.RED);
        k.getP2().getAdditionals().add(Color.NATURAL);
        k.getP2().getAdditionals().add(Color.WHITE);
    }

    public void initPlayMove(Kube kube) {

        kube.getK3().clear();
        setKubeBase(kube);
        setPlayerOneMountain(kube);
        setPlayerTwoMountain(kube);
        kube.getHistory().clear();
        kube.setPhase(2);
        kube.setCurrentPlayer(kube.getP1());
    }

    private boolean areSameMountain(Mountain m1, Mountain m2) {
        if (m1.getBaseSize() != m2.getBaseSize()) {
            return false;
        }
        for (int i = 0; i < m1.getBaseSize(); i++) {
            for (int j = 0; j < m1.getBaseSize(); j++) {
                if (m1.getCase(i, j) != m2.getCase(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Mountain cloneMountain(Mountain m) {

        Mountain clone = new Mountain(m.getBaseSize());
        for (int i = 0; i < m.getBaseSize(); i++) {

            for (int j = 0; j < m.getBaseSize(); j++) {

                clone.setCase(i, j, m.getCase(i, j));
            }
        }
        return clone;
    }

    private void simpleUnPlayTest(Kube k, Move m) {

        initPlayMove(k);
        Mountain k3 = cloneMountain(k.getK3());
        Mountain p1 = cloneMountain(k.getP1().getMountain());
        ArrayList<Color> p1Additional = new ArrayList<>(k.getP1().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        k.playMove(m);
        k.unPlay();

        ArrayList<Color> p1Additional2 = new ArrayList<>(k.getP1().getAdditionals());

        assertTrue(areSameMountain(k3, k.getK3()));
        assertTrue(areSameMountain(p1, k.getP1().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);

        assertFalse(k.getHistory().getDone().contains(m));
    }
}