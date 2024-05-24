package kube;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kube.model.*;

public class PlayerTest {
    
    @Test
    public void addToAdditionalsTest() {

        Player player = new Player(1);
        player.setHasValidateBuilding(true);
        player.addToAdditionals(ModelColor.RED);
        player.addToAdditionals(ModelColor.BLUE);
        player.addToAdditionals(ModelColor.GREEN);

        assertEquals(3, player.getAdditionals().size());

        assertEquals(ModelColor.RED, player.getAdditionals().get(0));
        assertEquals(ModelColor.BLUE, player.getAdditionals().get(1));
        assertEquals(ModelColor.GREEN, player.getAdditionals().get(2));
    }

    @Test
    public void removeFromAdditionalsTest() {
            
        Player player = new Player(1);
        player.setHasValidateBuilding(true);
        player.addToAdditionals(ModelColor.RED);
        player.addToAdditionals(ModelColor.BLUE);
        player.addToAdditionals(ModelColor.GREEN);

        assertEquals(ModelColor.BLUE, player.removeFromAdditionals(1));
        assertEquals(ModelColor.GREEN, player.removeFromAdditionals(1));

        assertEquals(1, player.getAdditionals().size());

        assertEquals(ModelColor.RED, player.removeFromAdditionals(0));
        assertEquals(0, player.getAdditionals().size());
    }

    @Test
    public void addToMountainFromAvailableToBuildTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3, NATURAL=2, GREEN=1} 

        assertTrue(player.addToMountainFromAvailableToBuild(0, 0, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(0, 0));
        assertEquals(3, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(1, 0, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(1, 0));
        assertEquals(2, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(1, 1, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(1, 1));
        assertEquals(1, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(2, 0, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(2, 0));
        assertEquals(0, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        assertFalse(player.addToMountainFromAvailableToBuild(2, 1, ModelColor.RED));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 1));
        assertEquals(0, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(0, 0, ModelColor.BLUE));
        assertEquals(ModelColor.BLUE, player.getMountain().getCase(0, 0));
        assertEquals(1, player.getAvailaibleToBuild().get(ModelColor.BLUE).intValue());
        assertEquals(1, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());
    }

    @Test
    public void removeFromMountainTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3, NATURAL=2, GREEN=1} 

        player.addToMountainFromAvailableToBuild(0, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 1, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(2, 0, ModelColor.RED);

        player.setHasValidateBuilding(true);

        assertEquals(ModelColor.RED, player.removeFromMountain(0, 0));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(0, 0));

        assertEquals(ModelColor.RED, player.removeFromMountain(1, 0));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 0));

        assertEquals(ModelColor.RED, player.removeFromMountain(1, 1));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 1));

        assertEquals(ModelColor.RED, player.removeFromMountain(2, 0));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 0));

        assertEquals(ModelColor.EMPTY, player.removeFromMountain(2, 1));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 1));
    }

    @Test
    public void removeFromMountainToAvailableToBuildTest() {
        
        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3, NATURAL=2, GREEN=1} 

        player.addToMountainFromAvailableToBuild(0, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 1, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(2, 0, ModelColor.RED);

        player.removeFromMountainToAvailableToBuild(0, 0);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(0, 0));
        assertEquals(1, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(1, 0);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 0));
        assertEquals(2, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(1, 1);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 1));
        assertEquals(3, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(2, 0);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 0));
        assertEquals(4, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(2, 1);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 1));
        assertEquals(4, player.getAvailaibleToBuild().get(ModelColor.RED).intValue());
    }

    @Test
    public void serializationTest() {
            
        ObjectMapper mapper = new ObjectMapper();

        Player player = new Player(1);

        player.setName("roger");

        player.getMountain().setCase(1, 0, ModelColor.BLUE);
        player.getMountain().setCase(1, 1, ModelColor.WHITE);
        player.getMountain().setCase(2, 0, ModelColor.YELLOW);
        player.getMountain().setCase(2, 1, ModelColor.WHITE);
        player.getMountain().setCase(2, 2, ModelColor.BLUE);

        HashMap<ModelColor, Integer> cubes = new HashMap<>();

        cubes.put(ModelColor.RED, 4);
        cubes.put(ModelColor.BLUE, 3);
        cubes.put(ModelColor.GREEN, 2);
        cubes.put(ModelColor.YELLOW, 1);

        player.setAvailableToBuild(cubes);

        try {
            String playerJson = mapper.writeValueAsString(player);
            Player player2 = mapper.readValue(playerJson, Player.class);
            assertEquals(player, player2);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
