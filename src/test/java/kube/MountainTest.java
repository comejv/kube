package kube;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import kube.model.*;

/**
 * Unit test for simple App.
 */
public class MountainTest 
{
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
    public void mountainTest()
    {
        // Test to check if the mountain setting order doens't matter


        // Test if the 

        // Test if the clear method is working
        m1.clear();
        Mountain m3 = new Mountain(5);
        assertTrue(areSameMountain(m1, m3));

    }

    private boolean areSameMountain(Mountain m1, Mountain m2) {
        if (m1.getBaseSize() != m2.getBaseSize()) {
            return false;
        }
        for (int i = 0; i < m1.getBaseSize(); i++) {
            if (m1.getCase(i, 0) != m2.getCase(i, 0)) {
                return false;
            }
        }
        return true;
    }
}
