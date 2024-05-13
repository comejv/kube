package kube;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import kube.model.*;

public class PlayerTest {
    
    @Test
    public void addToAdditionalsTest() {

        Player player = new Player(1);
        player.addToAdditionals(Color.RED);
        player.addToAdditionals(Color.BLUE);
        player.addToAdditionals(Color.GREEN);

        assertEquals(3, player.getAdditionals().size());

        assertEquals(Color.RED, player.getAdditionals().get(0));
        assertEquals(Color.BLUE, player.getAdditionals().get(1));
        assertEquals(Color.GREEN, player.getAdditionals().get(2));
    }

    @Test
    public void removeFromAdditionalsTest() {
            
        Player player = new Player(1);
        player.addToAdditionals(Color.RED);
        player.addToAdditionals(Color.BLUE);
        player.addToAdditionals(Color.GREEN);

        assertEquals(Color.BLUE, player.removeFromAdditionals(1));
        assertEquals(Color.GREEN, player.removeFromAdditionals(1));

        assertEquals(1, player.getAdditionals().size());

        assertEquals(Color.RED, player.removeFromAdditionals(0));
        assertEquals(0, player.getAdditionals().size());
    }

    @Test
    public void addToMountainTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3, NATURAL=2, GREEN=1} 

        assertTrue(player.addToMountain(0, 0, Color.RED));
        assertEquals(Color.RED, player.getMountain().getCase(0, 0));
        assertEquals(3, player.getAvalaibleToBuild().get(Color.RED).intValue());

        assertTrue(player.addToMountain(1, 0, Color.RED));
        assertEquals(Color.RED, player.getMountain().getCase(1, 0));
        assertEquals(2, player.getAvalaibleToBuild().get(Color.RED).intValue());

        assertTrue(player.addToMountain(1, 1, Color.RED));
        assertEquals(Color.RED, player.getMountain().getCase(1, 1));
        assertEquals(1, player.getAvalaibleToBuild().get(Color.RED).intValue());

        assertTrue(player.addToMountain(2, 0, Color.RED));
        assertEquals(Color.RED, player.getMountain().getCase(2, 0));
        assertEquals(0, player.getAvalaibleToBuild().get(Color.RED).intValue());

        assertFalse(player.addToMountain(2, 1, Color.RED));
        assertEquals(Color.EMPTY, player.getMountain().getCase(2, 1));
        assertEquals(0, player.getAvalaibleToBuild().get(Color.RED).intValue());

        assertTrue(player.addToMountain(0, 0, Color.BLUE));
        assertEquals(Color.BLUE, player.getMountain().getCase(0, 0));
        assertEquals(1, player.getAvalaibleToBuild().get(Color.BLUE).intValue());
        assertEquals(1, player.getAvalaibleToBuild().get(Color.RED).intValue());
    }

    @Test
    public void removeFromMountainTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3, NATURAL=2, GREEN=1} 

        player.addToMountain(0, 0, Color.RED);
        player.addToMountain(1, 0, Color.RED);
        player.addToMountain(1, 1, Color.RED);
        player.addToMountain(2, 0, Color.RED);

        assertEquals(Color.RED, player.removeFromMountain(0, 0));
        assertEquals(Color.EMPTY, player.getMountain().getCase(0, 0));

        assertEquals(Color.RED, player.removeFromMountain(1, 0));
        assertEquals(Color.EMPTY, player.getMountain().getCase(1, 0));

        assertEquals(Color.RED, player.removeFromMountain(1, 1));
        assertEquals(Color.EMPTY, player.getMountain().getCase(1, 1));

        assertEquals(Color.RED, player.removeFromMountain(2, 0));
        assertEquals(Color.EMPTY, player.getMountain().getCase(2, 0));

        assertEquals(Color.EMPTY, player.removeFromMountain(2, 1));
        assertEquals(Color.EMPTY, player.getMountain().getCase(2, 1));
    }

    @Test
    public void removeFromMountainToAvailableToBuildTest() {
        
        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3, NATURAL=2, GREEN=1} 

        player.addToMountain(0, 0, Color.RED);
        player.addToMountain(1, 0, Color.RED);
        player.addToMountain(1, 1, Color.RED);
        player.addToMountain(2, 0, Color.RED);

        player.removeFromMountainToAvailableToBuild(0, 0);
        assertEquals(Color.EMPTY, player.getMountain().getCase(0, 0));
        assertEquals(1, player.getAvalaibleToBuild().get(Color.RED).intValue());

        player.removeFromMountainToAvailableToBuild(1, 0);
        assertEquals(Color.EMPTY, player.getMountain().getCase(1, 0));
        assertEquals(2, player.getAvalaibleToBuild().get(Color.RED).intValue());

        player.removeFromMountainToAvailableToBuild(1, 1);
        assertEquals(Color.EMPTY, player.getMountain().getCase(1, 1));
        assertEquals(3, player.getAvalaibleToBuild().get(Color.RED).intValue());

        player.removeFromMountainToAvailableToBuild(2, 0);
        assertEquals(Color.EMPTY, player.getMountain().getCase(2, 0));
        assertEquals(4, player.getAvalaibleToBuild().get(Color.RED).intValue());

        player.removeFromMountainToAvailableToBuild(2, 1);
        assertEquals(Color.EMPTY, player.getMountain().getCase(2, 1));
        assertEquals(4, player.getAvalaibleToBuild().get(Color.RED).intValue());
    }
}
