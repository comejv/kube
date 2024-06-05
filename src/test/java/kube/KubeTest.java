package kube;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import kube.model.ModelColor;
import kube.configuration.Config;
import kube.model.Kube;
import kube.model.Mountain;
import kube.model.action.move.*;

public class KubeTest {

    @Test
    public void personnalTest() {

        Kube k1 = null;
        Kube k2 = null;

        File directory = new File(Config.SAVING_PATH_DIRECTORY);
        if (!directory.exists()){
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        File ser_test = new File(Config.SAVING_PATH_DIRECTORY + "kube.ser");

        try (FileOutputStream fos = new FileOutputStream(ser_test);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            k1 = new Kube(); // Initialize your Kube object
            initPlayMove(k1);
            oos.writeObject(k1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fis = new FileInputStream(ser_test);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            k2 = (Kube) ois.readObject();
            // Use the deserialized Kube object
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        ser_test.delete();

        assertEquals(k1.getP1().getName(), k2.getP1().getName());
        assertEquals(k1.getP1().getId(), k2.getP1().getId());
        assertEquals(k1.getP1().getIsMountainValidated(),
                k2.getP1().getIsMountainValidated());
        assertTrue(areSameMountain(k1.getP1().getMountain(),
                k2.getP1().getMountain()));

        assertEquals(k1.getP2().getName(), k2.getP2().getName());
        assertEquals(k1.getP2().getId(), k2.getP2().getId());
        assertEquals(k1.getP2().getIsMountainValidated(),
                k2.getP2().getIsMountainValidated());
        assertTrue(areSameMountain(k1.getP2().getMountain(),
                k2.getP2().getMountain()));

        for (int i = 0; i < k1.getHistory().getDone().size(); i++) {
            assertEquals(k1.getHistory().getDone().get(i),
                    k2.getHistory().getDone().get(i));
        }

        for (int i = 0; i < k1.getHistory().getUndone().size(); i++) {
            assertEquals(k1.getHistory().getUndone().get(i),
                    k2.getHistory().getUndone().get(i));
        }
    }

    @Test
    public void kubeTest() {

        Kube kube = new Kube();
        // Verifying the game has been correctly initialized
        assertEquals(6, kube.getP1().getMountain().getBaseSize());
        assertEquals(6, kube.getP2().getMountain().getBaseSize());
        assertEquals(9, kube.getMountain().getBaseSize());
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getPhase());
    }

    @Test
    public void fillBagTest() {

        Kube kube = new Kube();
        // Filling the bag
        kube.fillBag();
        ArrayList<ModelColor> bag = kube.getBag();
        // Verifying the bag has been filled with the right number of cubes
        assertEquals(45, bag.size());
        int[] colors = new int[8];
        for (ModelColor c : bag) {
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
        // Verifiying the bag has at least 9 cubes of each color
        for (int i = 3; i < 8; i++) {
            assertEquals(colors[i], 9);
        }
    }

    @Test
    public void fillBaseTest() {

        Kube kube = new Kube();
        // Filling the bag
        kube.fillBag();
        // Filling the base
        kube.fillBase();
        // Verifying the base has been filled with the right number of cubes
        assertEquals(36, kube.getBag().size());

        // Counting the number of cubes of each color in the base
        ModelColor[] base = new ModelColor[9];
        for (int i = 0; i < 9; i++) {
            base[i] = kube.getMountain().getCase(8, i);
        }
        ArrayList<ModelColor> colors = new ArrayList<>();
        for (ModelColor c : base) {
            assertFalse(c == ModelColor.EMPTY);
            if (!colors.contains(c)) {
                colors.add(c);
            }
        }
        // Verifying the base has at least 4 different colors
        assertTrue(colors.size() >= 4);
    }

    @Test
    public void isPlayableWorkingTest() {

        Kube kube = new Kube();
        initPlayMove(kube);

        // MoveMM
        assertTrue(kube.isPlayable(new MoveMM(1, 0, 7, 0, ModelColor.BLUE)));
        assertTrue(kube.isPlayable(new MoveMM(1, 0, 7, 6, ModelColor.BLUE)));
        assertTrue(kube.isPlayable(new MoveMM(1, 0, 7, 7, ModelColor.BLUE)));

        // MoveMW
        assertTrue(kube.isPlayable(new MoveMW(1, 1)));

        // MoveAM
        assertTrue(kube.isPlayable(new MoveAM(7, 0, ModelColor.BLUE)));
        assertTrue(kube.isPlayable(new MoveAM(7, 6, ModelColor.BLUE)));
        assertTrue(kube.isPlayable(new MoveAM(7, 7, ModelColor.BLUE)));
        assertTrue(kube.isPlayable(new MoveAM(7, 0, ModelColor.RED)));
        assertTrue(kube.isPlayable(new MoveAM(7, 1, ModelColor.RED)));
        assertTrue(kube.isPlayable(new MoveAM(7, 0, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 1, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 2, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 3, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 4, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 5, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 6, ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAM(7, 7, ModelColor.NATURAL)));

        // MoveAW
        assertTrue(kube.isPlayable(new MoveAW()));

        // MoveMA
        kube.setPenalty(true);
        assertTrue(kube.isPlayable(new MoveMA(1, 0, ModelColor.RED)));
        assertTrue(kube.isPlayable(new MoveMA(1, 1, ModelColor.GREEN)));

        // MoveAA
        assertTrue(kube.isPlayable(new MoveAA(ModelColor.GREEN)));
        assertTrue(kube.isPlayable(new MoveAA(ModelColor.YELLOW)));
        assertTrue(kube.isPlayable(new MoveAA(ModelColor.BLACK)));
        assertTrue(kube.isPlayable(new MoveAA(ModelColor.RED)));
        assertTrue(kube.isPlayable(new MoveAA(ModelColor.NATURAL)));
        assertTrue(kube.isPlayable(new MoveAA(ModelColor.WHITE)));
    }

    @Test
    public void isPlayableNotWorkingTest() {

        Kube kube = new Kube();
        initPlayMove(kube);

        // MoveMM not removable
        assertFalse(kube.isPlayable(new MoveMM(0, 0, 7, 0, ModelColor.BLUE)));
        assertFalse(kube.isPlayable(new MoveMM(2, 2, 7, 0, ModelColor.BLUE)));

        // MoveMM not compatible
        assertFalse(kube.isPlayable(new MoveMM(1, 0, 7, 1, ModelColor.BLUE)));
        assertFalse(kube.isPlayable(new MoveMM(1, 0, 7, 2, ModelColor.BLUE)));

        // MoveMW not removable
        assertFalse(kube.isPlayable(new MoveMW(0, 0)));
        assertFalse(kube.isPlayable(new MoveMW(2, 1)));

        // MoveAM not removable
        assertFalse(kube.isPlayable(new MoveAM(7, 1, ModelColor.GREEN)));
        assertFalse(kube.isPlayable(new MoveAM(7, 2, ModelColor.YELLOW)));

        // MoveAM not compatible
        assertFalse(kube.isPlayable(new MoveAM(7, 4, ModelColor.BLUE)));
        assertFalse(kube.isPlayable(new MoveAM(7, 7, ModelColor.RED)));

        // MoveAW not removable
        kube.getP1().getAdditionals().remove(ModelColor.WHITE);
        assertFalse(kube.isPlayable(new MoveAW()));

        // MoveMA not removable
        kube.setPenalty(true);
        assertFalse(kube.isPlayable(new MoveMA(2, 0, ModelColor.YELLOW)));
        assertFalse(kube.isPlayable(new MoveMA(2, 1, ModelColor.WHITE)));
        assertFalse(kube.isPlayable(new MoveMA(2, 2, ModelColor.BLUE)));
        kube.setPenalty(false);

        // MoveMA not compatible
        assertFalse(kube.isPlayable(new MoveMA(1, 0, ModelColor.RED)));
        assertFalse(kube.isPlayable(new MoveMA(1, 1, ModelColor.GREEN)));

        // MoveAA not removable
        kube.setPenalty(true);
        assertFalse(kube.isPlayable(new MoveAA(ModelColor.BLUE)));
        kube.setPenalty(false);

        // MoveAA not compatible
        assertFalse(kube.isPlayable(new MoveAA(ModelColor.GREEN)));
        assertFalse(kube.isPlayable(new MoveAA(ModelColor.YELLOW)));
        assertFalse(kube.isPlayable(new MoveAA(ModelColor.BLACK)));
        assertFalse(kube.isPlayable(new MoveAA(ModelColor.RED)));
        assertFalse(kube.isPlayable(new MoveAA(ModelColor.NATURAL)));
    }

    @Test
    public void distributeCubesToPlayersTest() {

        Kube kube = new Kube();
        // Filling the bag
        kube.fillBag();
        // Distributing the cubes to players
        kube.distributeCubesToPlayers();

        // Verifying the players have the right number of cubes
        int sum = 0;
        for (int n : kube.getP1().getAvailableToBuild().values()) {
            sum += n;
        }
        assertEquals(21, sum);

        sum = 0;
        for (int n : kube.getP2().getAvailableToBuild().values()) {
            sum += n;
        }
        assertEquals(21, sum);
        // Verifying the bag having at the end 11 cubes
        assertEquals(11, kube.getBag().size());
    }

    @Test
    public void playMoveWorkingTest() {

        Kube kube = new Kube();

        // MoveMW
        // Initialisation of the cube
        initPlayMove(kube);
        // Creation of the move we want to test
        MoveMW mw = new MoveMW(1, 1);
        // Playing the move
        assertTrue(kube.playMove(mw));
        // Verifying the cube has been coorecly moved
        assertEquals(ModelColor.EMPTY, kube.getP1().getMountain().getCase(1, 1));
        int lastElementIndex = kube.getHistory().getDone().size() - 1;
        // Verifying the move has been added to the history
        assertEquals(mw, kube.getHistory().getDone().get(lastElementIndex));
        // Verifying the penality state
        assertFalse(kube.getPenalty());

        // MoveMM
        initPlayMove(kube);
        MoveMM mm = new MoveMM(1, 0, 7, 0, ModelColor.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(ModelColor.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 0));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        mm = new MoveMM(1, 0, 7, 5, ModelColor.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(ModelColor.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 5));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        mm = new MoveMM(1, 0, 7, 6, ModelColor.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(ModelColor.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 6));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        mm = new MoveMM(1, 0, 7, 7, ModelColor.BLUE);
        assertTrue(kube.playMove(mm));
        assertEquals(ModelColor.EMPTY, kube.getP1().getMountain().getCase(1, 0));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 7));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(mm, kube.getHistory().getDone().get(lastElementIndex));
        assertTrue(kube.getPenalty());

        // MoveAM
        initPlayMove(kube);
        MoveAM am = new MoveAM(7, 0, ModelColor.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 0));
        assertEquals(0, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 5, ModelColor.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 5));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 6, ModelColor.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 6));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 7, ModelColor.BLUE);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.BLUE, kube.getMountain().getCase(7, 7));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.BLUE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertTrue(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 0, ModelColor.RED);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.RED, kube.getMountain().getCase(7, 0));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 1, ModelColor.RED);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.RED, kube.getMountain().getCase(7, 1));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 5, ModelColor.RED);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.RED, kube.getMountain().getCase(7, 5));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        am = new MoveAM(7, 6, ModelColor.RED);
        assertTrue(kube.playMove(am));
        assertEquals(ModelColor.RED, kube.getMountain().getCase(7, 6));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        for (int i = 0; i < 7; i++) {
            initPlayMove(kube);
            am = new MoveAM(7, i, ModelColor.NATURAL);
            assertTrue(kube.playMove(am));
            assertEquals(ModelColor.NATURAL, kube.getMountain().getCase(7, i));
            assertFalse(kube.getP1().getAdditionals().contains(ModelColor.NATURAL));
            lastElementIndex = kube.getHistory().getDone().size() - 1;
            assertEquals(am, kube.getHistory().getDone().get(lastElementIndex));
            if (i == 4 || i == 7) {
                assertTrue(kube.getPenalty());
            } else {
                assertFalse(kube.getPenalty());
            }
        }

        // MoveAW
        initPlayMove(kube);
        MoveAW aw = new MoveAW();
        assertTrue(kube.playMove(aw));
        assertFalse(kube.getP1().getAdditionals().contains(ModelColor.WHITE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aw, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        // MoveMA
        initPlayMove(kube);
        kube.setPenalty(true);
        MoveMA ma = new MoveMA(1, 0, ModelColor.RED);
        assertTrue(kube.playMove(ma));
        assertEquals(ModelColor.EMPTY, kube.getP2().getMountain().getCase(1, 0));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(ma, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        kube.setPenalty(true);
        ma = new MoveMA(1, 1, ModelColor.GREEN);
        assertTrue(kube.playMove(ma));
        assertEquals(ModelColor.EMPTY, kube.getP2().getMountain().getCase(1, 1));
        assertEquals(1, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.GREEN));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(ma, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        // MoveAA
        initPlayMove(kube);
        kube.setPenalty(true);
        MoveAA aa = new MoveAA(ModelColor.GREEN);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.GREEN));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.GREEN));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        kube.setPenalty(true);
        aa = new MoveAA(ModelColor.YELLOW);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.YELLOW));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.YELLOW));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        kube.setPenalty(true);
        aa = new MoveAA(ModelColor.BLACK);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.BLACK));
        assertEquals(1, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.BLACK));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        kube.setPenalty(true);
        aa = new MoveAA(ModelColor.RED);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.RED));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.RED));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        kube.setPenalty(true);
        aa = new MoveAA(ModelColor.NATURAL);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.NATURAL));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.NATURAL));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());

        initPlayMove(kube);
        kube.setPenalty(true);
        aa = new MoveAA(ModelColor.WHITE);
        assertTrue(kube.playMove(aa));
        assertEquals(0, Collections.frequency(kube.getP2().getAdditionals(), ModelColor.WHITE));
        assertEquals(2, Collections.frequency(kube.getP1().getAdditionals(), ModelColor.WHITE));
        lastElementIndex = kube.getHistory().getDone().size() - 1;
        assertEquals(aa, kube.getHistory().getDone().get(lastElementIndex));
        assertFalse(kube.getPenalty());
    }

    @Test
    public void playMoveNotWorkingTest() {

        Kube kube = new Kube();
        initPlayMove(kube);
        kube.getP1().getMountain().setCase(0, 0, ModelColor.BLUE);

        // Move to a place there is already a cube
        boolean res = kube.playMove(new MoveMM(0, 0, 8, 0, kube.getP1().getMountain().getCase(0, 0)));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(ModelColor.BLUE, kube.getP1().getMountain().getCase(0, 0));

        // Move a cube where it should not be
        res = kube.playMove(new MoveMM(0, 0, 7, 8, kube.getP1().getMountain().getCase(0, 0)));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(ModelColor.BLUE, kube.getP1().getMountain().getCase(0, 0));

        // Move a cube on the wrong mountain's cubes
        res = kube.playMove(new MoveMM(0, 0, 7, 2, kube.getP1().getMountain().getCase(0, 0)));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(ModelColor.BLUE, kube.getP1().getMountain().getCase(0, 0));

        // Move a cube with the wrong color
        res = kube.playMove(new MoveMM(0, 0, 7, 0, ModelColor.RED));
        assertFalse(res);
        assertEquals(0, kube.getHistory().getDone().size());
        assertEquals(0, kube.getHistory().getUndone().size());
        assertEquals(1, kube.getCurrentPlayer().getId());
        assertEquals(ModelColor.BLUE, kube.getP1().getMountain().getCase(0, 0));
    }

    @Test
    public void unPlayTest() {

        Kube kube = new Kube();

        // MoveMW
        // Initialisation
        initPlayMove(kube);
        // Creation of the move we want to test
        Move move = new MoveMW(1, 1);
        // Cloning the global kube state
        Mountain k3 = kube.getMountain().clone();
        Mountain p1 = kube.getP1().getMountain().clone();
        Mountain p2 = kube.getP2().getMountain().clone();
        int p = kube.getCurrentPlayer().getId();
        ArrayList<ModelColor> p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        ArrayList<ModelColor> p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        // Playing and unplaying the move
        kube.playMove(move);
        kube.unPlay();

        // Verifying the global kube state has not changed
        ArrayList<ModelColor> p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        ArrayList<ModelColor> p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveMM
        // MoveMM(MoveMM(1, 0, 7, 0, Color.BLUE))
        initPlayMove(kube);
        move = new MoveMM(1, 0, 7, 0, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveMM(1, 0, 7, 5, Color.BLUE)
        initPlayMove(kube);
        move = new MoveMM(1, 0, 7, 5, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveMM(1, 0, 7, 6, Color.BLUE)
        initPlayMove(kube);
        move = new MoveMM(1, 0, 7, 6, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveMM(1, 0, 7, 7, Color.BLUE)
        initPlayMove(kube);
        move = new MoveMM(1, 0, 7, 7, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM
        // MoveAM(7, 0, Color.BLUE)
        initPlayMove(kube);
        move = new MoveAM(7, 0, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 5, Color.BLUE)
        initPlayMove(kube);
        move = new MoveAM(7, 5, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 6, Color.BLUE)
        initPlayMove(kube);
        move = new MoveAM(7, 6, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 7, Color.BLUE)
        initPlayMove(kube);
        move = new MoveAM(7, 7, ModelColor.BLUE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 0, Color.RED)
        initPlayMove(kube);
        move = new MoveAM(7, 0, ModelColor.RED);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 1, Color.RED)
        initPlayMove(kube);
        move = new MoveAM(7, 1, ModelColor.RED);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 5, Color.RED)
        initPlayMove(kube);
        move = new MoveAM(7, 5, ModelColor.RED);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, 6, Color.RED)
        initPlayMove(kube);
        move = new MoveAM(7, 6, ModelColor.RED);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveAM(7, i, Color.NATURAL)
        for (int i = 0; i < 7; i++) {

            initPlayMove(kube);
            move = new MoveAM(7, i, ModelColor.NATURAL);
            k3 = kube.getMountain().clone();
            p1 = kube.getP1().getMountain().clone();
            p2 = kube.getP2().getMountain().clone();
            p = kube.getCurrentPlayer().getId();
            p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
            p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

            p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
            p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

            kube.playMove(move);
            kube.unPlay();

            p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
            p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

            assertTrue(areSameMountain(k3, kube.getMountain()));
            assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
            assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

            p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
            p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

            assertEquals(p1Additional, p1Additional2);
            assertEquals(p2Additional, p2Additional2);
            assertEquals(p, kube.getCurrentPlayer().getId());

            assertFalse(kube.getHistory().getDone().contains(move));
            assertFalse(kube.getPenalty());
        }

        // MoveAW
        initPlayMove(kube);
        move = new MoveAW();
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertFalse(kube.getPenalty());

        // MoveMA
        // MoveMA(1, 0, Color.RED)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveMA(1, 0, ModelColor.RED);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveMA(1, 1, Color.GREEN)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveMA(1, 1, ModelColor.GREEN);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveAA
        // MoveAA(Color.GREEN)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveAA(ModelColor.GREEN);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveAA(Color.YELLOW)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveAA(ModelColor.YELLOW);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveAA(Color.BLACK)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveAA(ModelColor.BLACK);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveAA(Color.RED)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveAA(ModelColor.RED);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveAA(Color.NATURAL)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveAA(ModelColor.NATURAL);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());

        // MoveAA(Color.WHITE)
        initPlayMove(kube);
        kube.setPenalty(true);
        move = new MoveAA(ModelColor.WHITE);
        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p = kube.getCurrentPlayer().getId();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(move);
        kube.unPlay();

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);
        assertEquals(p, kube.getCurrentPlayer().getId());

        assertFalse(kube.getHistory().getDone().contains(move));
        assertTrue(kube.getPenalty());
    }

    @Test
    public void rePlayTest() {

        Kube kube = new Kube();

        // MoveMW
        // Initialisation
        initPlayMove(kube);
        // Creation of the move we want to test
        MoveMW mw = new MoveMW(1, 1);
        // Cloning all the kube state
        Mountain k3 = kube.getMountain().clone();
        Mountain p1 = kube.getP1().getMountain().clone();
        Mountain p2 = kube.getP2().getMountain().clone();
        int nbWhite = kube.getP1().getWhiteUsed();
        ArrayList<ModelColor> p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        ArrayList<ModelColor> p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        // Playing, unplaying and replaying the move
        kube.playMove(mw);
        kube.unPlay();
        kube.rePlay();

        // Changing to the expected state
        p1.remove(1, 1);
        nbWhite++;

        // Verifying that the state is the expected one
        ArrayList<ModelColor> p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        ArrayList<ModelColor> p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);

        assertTrue(kube.getHistory().getDone().contains(mw));
        assertFalse(kube.getHistory().getUndone().contains(mw));
        assertEquals(nbWhite, kube.getP1().getWhiteUsed());

        // MoveMM
        initPlayMove(kube);
        MoveMM mm = new MoveMM(1, 0, 7, 0, ModelColor.BLUE);

        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(mm);
        kube.unPlay();
        kube.rePlay();

        p1.remove(1, 0);
        k3.setCase(7, 0, ModelColor.BLUE);

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);

        assertTrue(kube.getHistory().getDone().contains(mm));
        assertFalse(kube.getHistory().getUndone().contains(mm));

        // MoveAM
        initPlayMove(kube);
        MoveAM am = new MoveAM(7, 0, ModelColor.BLUE);

        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(am);
        kube.unPlay();
        kube.rePlay();

        p1Additional.remove(ModelColor.BLUE);
        k3.setCase(7, 0, ModelColor.BLUE);

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);

        assertTrue(kube.getHistory().getDone().contains(am));
        assertFalse(kube.getHistory().getUndone().contains(am));

        // MoveAW
        initPlayMove(kube);
        MoveAW aw = new MoveAW();

        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        nbWhite = kube.getP1().getWhiteUsed();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(aw);
        kube.unPlay();
        kube.rePlay();

        p1Additional.remove(ModelColor.WHITE);
        nbWhite++;

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);

        assertTrue(kube.getHistory().getDone().contains(aw));
        assertFalse(kube.getHistory().getUndone().contains(aw));
        assertEquals(nbWhite, kube.getP1().getWhiteUsed());

        // MoveMA
        initPlayMove(kube);
        kube.setPenalty(true);
        MoveMA ma = new MoveMA(1, 0, ModelColor.RED);

        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        nbWhite = kube.getP1().getWhiteUsed();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(ma);
        kube.unPlay();
        kube.rePlay();

        p2.remove(1, 0);
        p1Additional.add(ModelColor.RED);

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);

        assertTrue(kube.getHistory().getDone().contains(ma));
        assertFalse(kube.getHistory().getUndone().contains(ma));

        // MoveAA
        initPlayMove(kube);
        kube.setPenalty(true);
        MoveAA aa = new MoveAA(ModelColor.GREEN);

        k3 = kube.getMountain().clone();
        p1 = kube.getP1().getMountain().clone();
        p2 = kube.getP2().getMountain().clone();
        p1Additional = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional = new ArrayList<>(kube.getP2().getAdditionals());

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        kube.playMove(aa);
        kube.unPlay();
        kube.rePlay();

        p2Additional.remove(ModelColor.GREEN);
        p1Additional.add(ModelColor.GREEN);

        p1Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        p1Additional2 = new ArrayList<>(kube.getP1().getAdditionals());
        p2Additional2 = new ArrayList<>(kube.getP2().getAdditionals());

        assertTrue(areSameMountain(k3, kube.getMountain()));
        assertTrue(areSameMountain(p1, kube.getP1().getMountain()));
        assertTrue(areSameMountain(p2, kube.getP2().getMountain()));

        p1Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());
        p2Additional2.sort((c1, c2) -> c1.getColorCode() - c2.getColorCode());

        assertEquals(p1Additional, p1Additional2);
        assertEquals(p2Additional, p2Additional2);

        assertTrue(kube.getHistory().getDone().contains(aa));
        assertFalse(kube.getHistory().getUndone().contains(aa));
    }

    @Test
    public void testSeededBag() {

        Kube kube = new Kube();
        // Filling first kube's the bag
        kube.fillBag(1);
        Kube kube2 = new Kube();
        // Filling the second kube's bag with the same seed
        kube2.fillBag(1);
        // Verifying that the two bags are the same
        for (int i = 0; i < kube.getBag().size(); i++) {
            assertEquals(kube.getBag().get(i), kube2.getBag().get(i));
        }
    }

    @Test
    public void moveSetTest() {

        Kube kube = new Kube();
        // Filling the bag
        kube.fillBag(1);
        // Filling the base
        kube.fillBase();
        // Distributing the cubes to the players
        kube.distributeCubesToPlayers();
        // Setting the current player state
        ModelColor[][] mountainP1 = new ModelColor[][] {
                { ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY,
                        ModelColor.EMPTY },
                { ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY,
                        ModelColor.EMPTY },
                { ModelColor.EMPTY, ModelColor.BLACK, ModelColor.EMPTY, ModelColor.EMPTY, ModelColor.EMPTY,
                        ModelColor.EMPTY },
                { ModelColor.RED, ModelColor.BLUE, ModelColor.GREEN, ModelColor.EMPTY, ModelColor.EMPTY,
                        ModelColor.EMPTY },
                { ModelColor.RED, ModelColor.BLUE, ModelColor.GREEN, ModelColor.WHITE, ModelColor.EMPTY,
                        ModelColor.EMPTY },
                { ModelColor.RED, ModelColor.BLUE, ModelColor.GREEN, ModelColor.NATURAL, ModelColor.YELLOW,
                        ModelColor.WHITE }
        };

        kube.getP1().getMountain().setMountain(mountainP1);
        kube.getP1().setIsMountainValidated(true);
        kube.getP1().addToAdditionals(ModelColor.NATURAL);
        kube.getP1().addToAdditionals(ModelColor.WHITE);
        kube.getP1().addToAdditionals(ModelColor.YELLOW);
        kube.setCurrentPlayer(kube.getP1());

        // Verifying the outgoing move set
        kube.setPhase(2);
        ArrayList<Move> moves = kube.moveSet();

        assertTrue(moves.contains(new MoveMM(2, 1, 7, 0, ModelColor.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 2, ModelColor.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 3, ModelColor.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 4, ModelColor.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 5, ModelColor.BLACK)));
        assertTrue(moves.contains(new MoveMM(2, 1, 7, 5, ModelColor.BLACK)));
        assertTrue(moves.contains(new MoveMW(5, 5)));
        assertTrue(moves.contains(new MoveAM(7, 0, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 1, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 2, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 3, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 4, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 5, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 6, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAM(7, 7, ModelColor.NATURAL)));
        assertTrue(moves.contains(new MoveAW()));
        assertTrue(moves.contains(new MoveAM(7, 6, ModelColor.YELLOW)));
        assertTrue(moves.contains(new MoveAM(7, 7, ModelColor.YELLOW)));
    }

    private void initPlayMove(Kube k) {

        k.getMountain().clear();

        // Seeting the kube's base
        k.getMountain().setCase(8, 0, ModelColor.BLUE);
        k.getMountain().setCase(8, 1, ModelColor.RED);
        k.getMountain().setCase(8, 2, ModelColor.GREEN);
        k.getMountain().setCase(8, 3, ModelColor.YELLOW);
        k.getMountain().setCase(8, 4, ModelColor.BLACK);
        k.getMountain().setCase(8, 5, ModelColor.BLACK);
        k.getMountain().setCase(8, 6, ModelColor.NATURAL);
        k.getMountain().setCase(8, 7, ModelColor.BLUE);
        k.getMountain().setCase(8, 8, ModelColor.BLUE);

        // Setting the first player's mountain
        Mountain m = new Mountain(3);
        k.getP1().getAdditionals().clear();

        m.setCase(1, 0, ModelColor.BLUE);
        m.setCase(1, 1, ModelColor.WHITE);
        m.setCase(2, 0, ModelColor.YELLOW);
        m.setCase(2, 1, ModelColor.WHITE);
        m.setCase(2, 2, ModelColor.BLUE);

        k.getP1().setMountain(m);

        k.getP1().getAdditionals().add(ModelColor.BLUE);
        k.getP1().getAdditionals().add(ModelColor.RED);
        k.getP1().getAdditionals().add(ModelColor.NATURAL);
        k.getP1().getAdditionals().add(ModelColor.WHITE);

        // Setting the second player's mountain
        m = new Mountain(3);
        k.getP2().getAdditionals().clear();

        m.setCase(1, 0, ModelColor.RED);
        m.setCase(1, 1, ModelColor.GREEN);
        m.setCase(2, 0, ModelColor.BLACK);
        m.setCase(2, 1, ModelColor.GREEN);
        m.setCase(2, 2, ModelColor.RED);

        k.getP2().setMountain(m);

        k.getP2().getAdditionals().add(ModelColor.GREEN);
        k.getP2().getAdditionals().add(ModelColor.YELLOW);
        k.getP2().getAdditionals().add(ModelColor.BLACK);
        k.getP2().getAdditionals().add(ModelColor.RED);
        k.getP2().getAdditionals().add(ModelColor.NATURAL);
        k.getP2().getAdditionals().add(ModelColor.WHITE);

        // Other settings for the kube instance
        k.getHistory().clear();
        k.setPhase(2);
        k.setCurrentPlayer(k.getP1());
        k.getP1().setIsMountainValidated(true);
        k.getP2().setIsMountainValidated(true);
        k.setPenalty(false);
    }

    // @Test
    // public void serializationTest() {

    // ObjectMapper om = new ObjectMapper();

    // // Preparation phase serialization
    // Kube kube = new Kube();

    // kube.init(null, null, 0);

    // kube.getP1().addToMountainFromAvailableToBuild(0, 0, ModelColor.RED);
    // kube.getP1().addToMountainFromAvailableToBuild(4, 0, ModelColor.GREEN);
    // kube.getP1().addToMountainFromAvailableToBuild(4, 1, ModelColor.GREEN);
    // kube.getP1().addToMountainFromAvailableToBuild(4, 2, ModelColor.GREEN);
    // kube.getP1().addToMountainFromAvailableToBuild(4, 3, ModelColor.GREEN);
    // kube.getP1().addToMountainFromAvailableToBuild(3, 0, ModelColor.BLACK);
    // kube.getP1().addToMountainFromAvailableToBuild(3, 0, ModelColor.BLACK);

    // try {
    // String s = om.writeValueAsString(kube);

    // Kube k = om.readValue(s, Kube.class);

    // assertEquals(kube.getPhase(), k.getPhase());

    // assertTrue(areSameMountain(kube.getK3(), k.getK3()));

    // assertEquals(kube.getP1().getName(), k.getP1().getName());
    // assertEquals(kube.getP1().getId(), k.getP1().getId());
    // assertEquals(kube.getP1().getHasValidateBuilding(),
    // k.getP1().getHasValidateBuilding());
    // assertTrue(areSameMountain(kube.getP1().getMountain(),
    // k.getP1().getMountain()));

    // assertEquals(kube.getP2().getName(), k.getP2().getName());
    // assertEquals(kube.getP2().getId(), k.getP2().getId());
    // assertEquals(kube.getP2().getHasValidateBuilding(),
    // k.getP2().getHasValidateBuilding());
    // assertTrue(areSameMountain(kube.getP2().getMountain(),
    // k.getP2().getMountain()));

    // assertEquals(kube.getHistory().getFirstPlayer(),
    // k.getHistory().getFirstPlayer());

    // for (int i = 0; i < kube.getHistory().getDone().size(); i++) {
    // assertEquals(kube.getHistory().getDone().get(i),
    // k.getHistory().getDone().get(i));
    // }

    // for (int i = 0; i < kube.getHistory().getUndone().size(); i++) {
    // assertEquals(kube.getHistory().getUndone().get(i),
    // k.getHistory().getUndone().get(i));
    // }

    // } catch (JsonProcessingException e) {
    // e.printStackTrace();
    // }

    // // Game phase serialization
    // kube = new Kube();

    // initPlayMove(kube);

    // kube.getP1().setHasValidateBuilding(true);
    // kube.getP2().setHasValidateBuilding(true);

    // kube.getP1().setInitialMountain(kube.getP1().getMountain().clone());
    // kube.getP2().setInitialMountain(kube.getP2().getMountain().clone());

    // try {
    // String s = om.writeValueAsString(kube);

    // Kube k = om.readValue(s, Kube.class);

    // assertEquals(kube.getPhase(), k.getPhase());

    // assertTrue(areSameMountain(kube.getK3(), k.getK3()));

    // assertEquals(kube.getP1().getName(), k.getP1().getName());
    // assertEquals(kube.getP1().getId(), k.getP1().getId());
    // assertEquals(kube.getP1().getHasValidateBuilding(),
    // k.getP1().getHasValidateBuilding());
    // assertTrue(areSameMountain(kube.getP1().getInitialMountain(),
    // k.getP1().getInitialMountain()));
    // assertTrue(areSameMountain(kube.getP1().getMountain(),
    // k.getP1().getMountain()));

    // assertEquals(kube.getP2().getName(), k.getP2().getName());
    // assertEquals(kube.getP2().getId(), k.getP2().getId());
    // assertEquals(kube.getP2().getHasValidateBuilding(),
    // k.getP2().getHasValidateBuilding());
    // assertTrue(areSameMountain(kube.getP2().getInitialMountain(),
    // k.getP2().getInitialMountain()));
    // assertTrue(areSameMountain(kube.getP2().getMountain(),
    // k.getP2().getMountain()));

    // assertEquals(kube.getHistory().getFirstPlayer(),
    // k.getHistory().getFirstPlayer());

    // for (int i = 0; i < kube.getHistory().getDone().size(); i++) {
    // assertEquals(kube.getHistory().getDone().get(i),
    // k.getHistory().getDone().get(i));
    // }

    // for (int i = 0; i < kube.getHistory().getUndone().size(); i++) {
    // assertEquals(kube.getHistory().getUndone().get(i),
    // k.getHistory().getUndone().get(i));
    // }

    // } catch (JsonProcessingException e) {
    // e.printStackTrace();
    // }
    // }

    // @Test
    // public void saveInstanceTest() {

    // Kube kube = new Kube();

    // initPlayMove(kube);

    // kube.getP1().setHasValidateBuilding(true);
    // kube.getP2().setHasValidateBuilding(true);

    // kube.getP1().setInitialMountain(kube.getP1().getMountain().clone());
    // kube.getP2().setInitialMountain(kube.getP2().getMountain().clone());

    // kube.saveInstance("test.json");

    // Kube k = new Kube("test.json");

    // assertEquals(kube.getPhase(), k.getPhase());

    // assertTrue(areSameMountain(kube.getK3(), k.getK3()));

    // assertEquals(kube.getP1().getName(), k.getP1().getName());
    // assertEquals(kube.getP1().getId(), k.getP1().getId());
    // assertEquals(kube.getP1().getHasValidateBuilding(),
    // k.getP1().getHasValidateBuilding());
    // assertTrue(areSameMountain(kube.getP1().getInitialMountain(),
    // k.getP1().getInitialMountain()));
    // assertTrue(areSameMountain(kube.getP1().getMountain(),
    // k.getP1().getMountain()));

    // assertEquals(kube.getP2().getName(), k.getP2().getName());
    // assertEquals(kube.getP2().getId(), k.getP2().getId());
    // assertEquals(kube.getP2().getHasValidateBuilding(),
    // k.getP2().getHasValidateBuilding());
    // assertTrue(areSameMountain(kube.getP2().getInitialMountain(),
    // k.getP2().getInitialMountain()));
    // assertTrue(areSameMountain(kube.getP2().getMountain(),
    // k.getP2().getMountain()));

    // assertEquals(kube.getHistory().getFirstPlayer(),
    // k.getHistory().getFirstPlayer());

    // for (int i = 0; i < kube.getHistory().getDone().size(); i++) {
    // assertEquals(kube.getHistory().getDone().get(i),
    // k.getHistory().getDone().get(i));
    // }

    // for (int i = 0; i < kube.getHistory().getUndone().size(); i++) {
    // assertEquals(kube.getHistory().getUndone().get(i),
    // k.getHistory().getUndone().get(i));
    // }

    // File file = new File(Config.SAVING_PATH_DIRECTORY + "test.json");

    // file.delete();
    // }

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
}
