package kube;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;

import kube.model.*;

public class MountainTest {

    @Test
    public void simpleSetTest() {

        Mountain m = new Mountain(5);
        m.setCase(0, 0, ModelColor.RED);
        assertTrue(m.getCase(0, 0) == ModelColor.RED);
    }

    @Test
    public void simpleRemoveTest() {

        Mountain m1 = new Mountain(5);
        m1.setCase(0, 0, ModelColor.RED);
        m1.remove(0, 0);
        assertTrue(m1.getCase(0, 0) == ModelColor.EMPTY);
        Mountain m2 = new Mountain(5);

        assertEquals(m1, m2);
    }

    @Test
    public void multipleSetTest() {

        Mountain m1 = new Mountain(5);
        Mountain m2 = new Mountain(5);

        m1.setCase(0, 0, ModelColor.BLUE);
        m1.setCase(1, 0, ModelColor.RED);
        m1.setCase(2, 0, ModelColor.GREEN);
        m1.setCase(3, 0, ModelColor.YELLOW);
        m1.setCase(4, 0, ModelColor.WHITE);

        m2.setCase(4, 0, ModelColor.WHITE);
        m2.setCase(1, 0, ModelColor.RED);
        m2.setCase(2, 0, ModelColor.GREEN);
        m2.setCase(3, 0, ModelColor.YELLOW);
        m2.setCase(0, 0, ModelColor.BLUE);

        assertEquals(m1, m2);
    }

    @Test
    public void multipleRemoveTest() {

        Mountain m1 = new Mountain(5);
        Mountain m2 = new Mountain(5);

        m1.setCase(0, 0, ModelColor.BLUE);
        m1.setCase(1, 0, ModelColor.RED);
        m1.setCase(2, 0, ModelColor.GREEN);
        m1.setCase(3, 0, ModelColor.YELLOW);
        m1.setCase(4, 0, ModelColor.WHITE);

        m1.remove(0, 0);
        m1.remove(1, 0);
        m1.remove(2, 0);
        m1.remove(3, 0);
        m1.remove(4, 0);

        assertEquals(m1, m2);
    }

    @Test
    public void clearTest() {

        Mountain m1 = new Mountain(5);
        Mountain m2 = new Mountain(5);

        m1.setCase(0, 0, ModelColor.BLUE);
        m1.setCase(1, 0, ModelColor.RED);
        m1.setCase(2, 0, ModelColor.GREEN);
        m1.setCase(3, 0, ModelColor.YELLOW);
        m1.setCase(4, 0, ModelColor.WHITE);

        m1.clear();

        assertEquals(m1, m2);
    }

    @Test
    public void notOverflowSimpleSetTest() {

        Mountain m = new Mountain(5);
        m.setCase(0, 0, ModelColor.BLUE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 0 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.BLUE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.EMPTY);
                }
            }
        }
    }

    @Test
    public void notOverflowMultipleColumnSetTest() {

        Mountain m = new Mountain(5);

        m.setCase(0, 0, ModelColor.BLUE);
        m.setCase(1, 0, ModelColor.RED);
        m.setCase(2, 0, ModelColor.GREEN);
        m.setCase(3, 0, ModelColor.YELLOW);
        m.setCase(4, 0, ModelColor.WHITE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 0 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.BLUE);
                } else if (etage == 1 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.RED);
                } else if (etage == 2 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.GREEN);
                } else if (etage == 3 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.YELLOW);
                } else if (etage == 4 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.WHITE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.EMPTY);
                }
            }
        }
    }

    @Test
    public void notOverflowMultipleLineSetTest() {

        Mountain m = new Mountain(5);

        m.setCase(4, 0, ModelColor.BLUE);
        m.setCase(4, 1, ModelColor.RED);
        m.setCase(4, 2, ModelColor.GREEN);
        m.setCase(4, 3, ModelColor.YELLOW);
        m.setCase(4, 4, ModelColor.WHITE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 4 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.BLUE);
                } else if (etage == 4 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.RED);
                } else if (etage == 4 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.GREEN);
                } else if (etage == 4 && colonne == 3) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.YELLOW);
                } else if (etage == 4 && colonne == 4) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.WHITE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.EMPTY);
                }
            }
        }
    }

    @Test
    public void notOverflowAllMountainSetTest() {

        Mountain m = new Mountain(5);

        m.setCase(0, 0, ModelColor.BLUE);
        m.setCase(1, 0, ModelColor.RED);
        m.setCase(1, 1, ModelColor.GREEN);
        m.setCase(2, 0, ModelColor.YELLOW);
        m.setCase(2, 1, ModelColor.WHITE);
        m.setCase(2, 2, ModelColor.BLUE);
        m.setCase(3, 0, ModelColor.RED);
        m.setCase(3, 1, ModelColor.GREEN);
        m.setCase(3, 2, ModelColor.YELLOW);
        m.setCase(3, 3, ModelColor.WHITE);
        m.setCase(4, 0, ModelColor.BLUE);
        m.setCase(4, 1, ModelColor.RED);
        m.setCase(4, 2, ModelColor.GREEN);
        m.setCase(4, 3, ModelColor.YELLOW);
        m.setCase(4, 4, ModelColor.WHITE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 0 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.BLUE);
                } else if (etage == 1 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.RED);
                } else if (etage == 1 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.GREEN);
                } else if (etage == 2 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.YELLOW);
                } else if (etage == 2 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.WHITE);
                } else if (etage == 2 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.BLUE);
                } else if (etage == 3 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.RED);
                } else if (etage == 3 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.GREEN);
                } else if (etage == 3 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.YELLOW);
                } else if (etage == 3 && colonne == 3) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.WHITE);
                } else if (etage == 4 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.BLUE);
                } else if (etage == 4 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.RED);
                } else if (etage == 4 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.GREEN);
                } else if (etage == 4 && colonne == 3) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.YELLOW);
                } else if (etage == 4 && colonne == 4) {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.WHITE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == ModelColor.EMPTY);
                }
            }
        }
    }

    @Test
    public void removableTest() {

        Mountain m = new Mountain(3);

        m.setCase(2, 0, ModelColor.BLUE);
        m.setCase(2, 1, ModelColor.BLUE);
        m.setCase(2, 2, ModelColor.BLUE);

        m.setCase(1, 0, ModelColor.RED);
        m.setCase(1, 1, ModelColor.RED);

        m.setCase(0, 0, ModelColor.GREEN);

        ArrayList<Point> r = m.removable();
        assertTrue(r.size() == 1);

        Point p = r.get(0);
        assertTrue(m.getCase(p.x, p.y) == ModelColor.GREEN);
    }

    @Test
    public void compatibleTest() {

        Mountain m = new Mountain(3);

        m.setCase(2, 0, ModelColor.BLUE);
        m.setCase(2, 1, ModelColor.RED);
        m.setCase(2, 2, ModelColor.RED);

        Point p1 = new Point(1, 0);
        Point p2 = new Point(1, 1);
        assertTrue(m.compatible(ModelColor.GREEN).isEmpty());
        assertTrue(m.compatible(ModelColor.YELLOW).isEmpty());
        assertTrue(m.compatible(ModelColor.BLACK).isEmpty());

        assertTrue(m.compatible(ModelColor.NATURAL).size() == 2);
        assertTrue(m.compatible(ModelColor.NATURAL).contains(p1));
        assertTrue(m.compatible(ModelColor.NATURAL).contains(p2));

        assertTrue(m.compatible(ModelColor.RED).size() == 2);
        assertTrue(m.compatible(ModelColor.RED).contains(p1));
        assertTrue(m.compatible(ModelColor.RED).contains(p2));

        assertTrue(m.compatible(ModelColor.BLUE).size() == 1);
        assertTrue(m.compatible(ModelColor.BLUE).contains(p1));
        assertTrue(!(m.compatible(ModelColor.BLUE).contains(p2)));
    }

    @Test
    public void isPenalityTest() {

        Mountain m = new Mountain(5);

        m.setCase(4, 0, ModelColor.BLUE);
        m.setCase(4, 1, ModelColor.RED);
        m.setCase(4, 2, ModelColor.RED);
        m.setCase(4, 3, ModelColor.NATURAL);
        m.setCase(4, 4, ModelColor.NATURAL);

        assertFalse(m.isPenality(3, 0));
        assertTrue(m.isPenality(3, 1));
        assertFalse(m.isPenality(3, 2));
        assertTrue(m.isPenality(3, 3));
    }
}
