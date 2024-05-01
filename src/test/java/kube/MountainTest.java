package kube;

import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.junit.Test;

import kube.model.*;

/**
 * Unit test for simple App.
 */
public class MountainTest {
    @Test
    public void simpleSetTest() {

        Mountain m = new Mountain(5);
        m.setCase(0, 0, Color.RED);
        assertTrue(m.getCase(0, 0) == Color.RED);
    }

    @Test
    public void simpleRemoveTest() {

        Mountain m1 = new Mountain(5);
        m1.setCase(0, 0, Color.RED);
        m1.remove(0, 0);
        assertTrue(m1.getCase(0, 0) == Color.EMPTY);
        Mountain m2 = new Mountain(5);
        assertTrue(areSameMountain(m1, m2));
    }

    @Test
    public void multipleSetTest() {

        Mountain m1 = new Mountain(5);
        Mountain m2 = new Mountain(5);

        m1.setCase(0, 0, Color.BLUE);
        m1.setCase(1, 0, Color.RED);
        m1.setCase(2, 0, Color.GREEN);
        m1.setCase(3, 0, Color.YELLOW);
        m1.setCase(4, 0, Color.WHITE);

        m2.setCase(4, 0, Color.WHITE);
        m2.setCase(1, 0, Color.RED);
        m2.setCase(2, 0, Color.GREEN);
        m2.setCase(3, 0, Color.YELLOW);
        m2.setCase(0, 0, Color.BLUE);

        assertTrue(areSameMountain(m1, m2));
    }

    @Test
    public void multipleRemoveTest() {

        Mountain m1 = new Mountain(5);
        Mountain m2 = new Mountain(5);

        m1.setCase(0, 0, Color.BLUE);
        m1.setCase(1, 0, Color.RED);
        m1.setCase(2, 0, Color.GREEN);
        m1.setCase(3, 0, Color.YELLOW);
        m1.setCase(4, 0, Color.WHITE);

        m1.remove(0, 0);
        m1.remove(1, 0);
        m1.remove(2, 0);
        m1.remove(3, 0);
        m1.remove(4, 0);

        assertTrue(areSameMountain(m1, m2));
    }

    @Test
    public void clearTest() {

        Mountain m1 = new Mountain(5);
        Mountain m2 = new Mountain(5);

        m1.setCase(0, 0, Color.BLUE);
        m1.setCase(1, 0, Color.RED);
        m1.setCase(2, 0, Color.GREEN);
        m1.setCase(3, 0, Color.YELLOW);
        m1.setCase(4, 0, Color.WHITE);

        m1.clear();

        assertTrue(areSameMountain(m1, m2));
    }

    @Test
    public void notOverflowSimpleSetTest() {

        Mountain m = new Mountain(5);
        m.setCase(0, 0, Color.BLUE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 0 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.BLUE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == Color.EMPTY);
                }
            }
        }
    }

    @Test
    public void notOverflowMultipleColumnSetTest() {

        Mountain m = new Mountain(5);

        m.setCase(0, 0, Color.BLUE);
        m.setCase(1, 0, Color.RED);
        m.setCase(2, 0, Color.GREEN);
        m.setCase(3, 0, Color.YELLOW);
        m.setCase(4, 0, Color.WHITE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 0 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.BLUE);
                } else if (etage == 1 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.RED);
                } else if (etage == 2 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.GREEN);
                } else if (etage == 3 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.YELLOW);
                } else if (etage == 4 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.WHITE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == Color.EMPTY);
                }
            }
        }
    }

    @Test
    public void notOverflowMultipleLineSetTest() {
            
        Mountain m = new Mountain(5);

        m.setCase(4, 0, Color.BLUE);
        m.setCase(4, 1, Color.RED);
        m.setCase(4, 2, Color.GREEN);
        m.setCase(4, 3, Color.YELLOW);
        m.setCase(4, 4, Color.WHITE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 4 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.BLUE);
                } else if (etage == 4 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == Color.RED);
                } else if (etage == 4 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == Color.GREEN);
                } else if (etage == 4 && colonne == 3) {
                    assertTrue(m.getCase(etage, colonne) == Color.YELLOW);
                } else if (etage == 4 && colonne == 4) {
                    assertTrue(m.getCase(etage, colonne) == Color.WHITE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == Color.EMPTY);
                }
            }
        }
    }
    
    @Test
    public void notOverflowAllMountainSetTest() {

        Mountain m = new Mountain(5);

        m.setCase(0, 0, Color.BLUE);
        m.setCase(1, 0, Color.RED);
        m.setCase(1, 1, Color.GREEN);
        m.setCase(2, 0, Color.YELLOW);
        m.setCase(2, 1, Color.WHITE);
        m.setCase(2, 2, Color.BLUE);
        m.setCase(3, 0, Color.RED);
        m.setCase(3, 1, Color.GREEN);
        m.setCase(3, 2, Color.YELLOW);
        m.setCase(3, 3, Color.WHITE);
        m.setCase(4, 0, Color.BLUE);
        m.setCase(4, 1, Color.RED);
        m.setCase(4, 2, Color.GREEN);
        m.setCase(4, 3, Color.YELLOW);
        m.setCase(4, 4, Color.WHITE);

        for (int etage = 0; etage < m.getBaseSize(); etage++) {
            for (int colonne = 0; colonne < etage + 1; colonne++) {
                if (etage == 0 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.BLUE);
                } else if (etage == 1 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.RED);
                } else if (etage == 1 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == Color.GREEN);
                } else if (etage == 2 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.YELLOW);
                } else if (etage == 2 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == Color.WHITE);
                } else if (etage == 2 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == Color.BLUE);
                } else if (etage == 3 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.RED);
                } else if (etage == 3 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == Color.GREEN);
                } else if (etage == 3 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == Color.YELLOW);
                } else if (etage == 3 && colonne == 3) {
                    assertTrue(m.getCase(etage, colonne) == Color.WHITE);
                } else if (etage == 4 && colonne == 0) {
                    assertTrue(m.getCase(etage, colonne) == Color.BLUE);
                } else if (etage == 4 && colonne == 1) {
                    assertTrue(m.getCase(etage, colonne) == Color.RED);
                } else if (etage == 4 && colonne == 2) {
                    assertTrue(m.getCase(etage, colonne) == Color.GREEN);
                } else if (etage == 4 && colonne == 3) {
                    assertTrue(m.getCase(etage, colonne) == Color.YELLOW);
                } else if (etage == 4 && colonne == 4) {
                    assertTrue(m.getCase(etage, colonne) == Color.WHITE);
                } else {
                    assertTrue(m.getCase(etage, colonne) == Color.EMPTY);
                }
            }
        }
    }

    @Test
    public void removableTest() {

        Mountain m = new Mountain(3);

        m.setCase(2, 0, Color.BLUE);
        m.setCase(2, 1, Color.BLUE);
        m.setCase(2, 2, Color.BLUE);
        
        m.setCase(1, 0, Color.RED);
        m.setCase(1, 1, Color.RED);

        m.setCase(0, 0, Color.GREEN);

        ArrayList<Point> r = m.removable();
        assertTrue(r.size() == 1);

        Point p = r.get(0);
        assertTrue(m.getCase((int) p.getX(), (int) p.getY()) == Color.GREEN);
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
}